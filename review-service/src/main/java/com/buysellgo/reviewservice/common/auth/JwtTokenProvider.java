package com.buysellgo.reviewservice.common.auth;

import com.buysellgo.reviewservice.common.entity.Role;
import com.buysellgo.reviewservice.common.exception.CustomException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static com.buysellgo.reviewservice.common.util.CommonConstant.BEARER_PREFIX;
import static com.buysellgo.reviewservice.common.util.CommonConstant.INVALID_TOKEN;

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
    public TokenUserInfo validateAndGetTokenUserInfo(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey(secretKey))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            log.info("claims : {}", claims);

            return TokenUserInfo.builder()
                    .email(claims.getSubject())
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

    public TokenUserInfo getTokenUserInfo(String accessToken) {
        if (!accessToken.startsWith(BEARER_PREFIX.getValue())) {
            throw new CustomException(INVALID_TOKEN.getValue());
        }

        // 토큰에서 사용자 정보 추출
        String token = accessToken.replace(BEARER_PREFIX.getValue(), "");
        return validateAndGetTokenUserInfo(token);
    }
}

















