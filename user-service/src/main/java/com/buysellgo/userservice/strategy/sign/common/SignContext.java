package com.buysellgo.userservice.strategy.sign.common;

import com.buysellgo.userservice.common.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * Strategy 패턴의 Context 클래스
 * 적절한 회원가입/탈퇴 전략을 선택하고 관리하는 책임을 가짐
 */
@Component
@RequiredArgsConstructor
public class SignContext {
    /**
     * Spring이 자동으로 모든 SignStrategy 구현체들을 주입
     * 새로운 회원 유형이 추가되면 자동으로 전략 목록에 포함됨
     */
    private final List<SignStrategy<?>> strategies;

    /**
     * 사용자 역할에 맞는 전략을 찾아서 반환
     * @param role 사용자 역할 (SELLER, BUYER 등)
     * @return 해당 역할에 맞는 회원가입/탈퇴 전략
     * @throws IllegalArgumentException 지원하지 않는 역할인 경우
     */
    @SuppressWarnings("unchecked")
    public <T> SignStrategy<T> getStrategy(Role role) {
        return (SignStrategy<T>) strategies.stream()
            .filter(strategy -> strategy.supports(role))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Unsupported role: " + role));
    }
}
