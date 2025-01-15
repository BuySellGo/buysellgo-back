package com.buysellgo.helpdeskservice.common.auth;

import com.buysellgo.helpdeskservice.common.entity.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@Slf4j
// 역할: 토큰을 발급하고, 서명 위조를 검사하는 객체
public class JwtTokenProvider {

    // 서명에 사용할 값 (512비트 이상의 랜덤 문자열을 권장)
    // yml에 있는 값 땡겨오기 (properties 방식으로 선언)
    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private int expiration;

    @Value("${jwt.secretKeyRt}")
    private String secreKeyRt;

    @Value("${jwt.expirationRt}")
    private int expirationRt;

    private SecretKey getSigningKey(String secretKey) {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // 토큰 생성 메서드
     /*
            {
                "iss": "서비스 이름(발급자)",
                "exp": "2023-12-27(만료일자)",
                "iat": "2023-11-27(발급일자)",
                "email": "로그인한 사람 이메일",
                "role": "Premium"
                ...
                == 서명
            }
     */
    public String createToken(String email, String role, long id) {
        // Claims: 페이로드에 들어갈 사용자 정보
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("role", role);
        claims.put("id", id);
        Date date = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(date)
                //현재 시간 밀리초에 30분을 더한 시간을 만료시간으로 세팅
                .setExpiration(new Date(date.getTime() + expiration * 60 * 1000L))
                .signWith(getSigningKey(secretKey))
                .compact();
    }

    public String createRefreshToken(String email, String role) {
        // Claims: 페이로드에 들어갈 사용자 정보
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("role", role);
        Date date = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(date)
                //현재 시간 밀리초에 30분을 더한 시간을 만료시간으로 세팅
                .setExpiration(new Date(date.getTime() + expirationRt * 60 * 1000L))
                .signWith(getSigningKey(secreKeyRt))
                .compact();
    }


    /**
     * 클라이언트가 전송한 토큰을 디코딩하여 토큰의 위조 여부를 확인
     * 토큰을 json으로 파싱해서 클레임(토큰 정보)을 리턴
     *
     * @param token - 필터가 전달해 준 토큰
     * @return - 토큰 안에 있는 인증된 유저 정보를 반환
     */
    public TokenUserInfo validateAndGetTokenUserInfo(String token) throws Exception {

        try {
            Claims claims = Jwts.parserBuilder()
                    // 토큰 발급자의 발급 당시의 서명을 넣어줌.
                    .setSigningKey(getSigningKey(secretKey))
                    // 서명 위조 검사: 위조된 경우에는 예외가 발생합니다.
                    // 위조되지 않았다면 payload를 리턴.
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            log.info("claims : {}", claims);

            return TokenUserInfo.builder()
                    .email(claims.getSubject())
                    // 클레임이 get 할 수 있는 타입이 정해져 있어서 Role을 못 꺼냅니다.
                    // 일단 String으로 꺼내고, 다시 Role 타입으로 포장해서 집어 넣겠습니다.
                    .role(Role.valueOf(claims.get("role", String.class)))
                    .id(claims.get("id", Long.class))
                    .build();
        } catch (ExpiredJwtException e) {
            log.error("Token expired: {}", e.getMessage());
            throw new JwtException("만료된 토큰입니다.");
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
            throw new JwtException("유효하지 않은 토큰 서명입니다.");
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            throw new JwtException("유효하지 않은 토큰 형식입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token: {}", e.getMessage());
            throw new JwtException("지원되지 않는 토큰 형식입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
            throw new JwtException("토큰이 비어있습니다.");
        }
    }
}

















