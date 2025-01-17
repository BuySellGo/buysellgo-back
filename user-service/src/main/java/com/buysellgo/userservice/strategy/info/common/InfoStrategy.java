package com.buysellgo.userservice.strategy.info.common;

import java.util.Map;

import com.buysellgo.userservice.common.entity.Role;
import com.buysellgo.userservice.controller.info.dto.InfoUpdateReq; 
/**
 * 회원 정보를 조회하고 수정하는 역할을 하는 전략 인터페이스입니다.
 * 이 인터페이스는 회원 정보의 검색, 목록 조회, 수정 및 역할 지원을 위한 메서드를 정의합니다.
 *
 * @param <T> String 키와 Object 값을 가지는 Map을 확장하는 데이터 구조의 타입입니다.
 */
public interface InfoStrategy<T extends Map<String, Object>> {

    /**
     * 단일 회원 정보를 검색합니다.
     *
     * @return T 타입의 단일 회원 정보를 포함하는 infoResult를 반환합니다.
     */
    InfoResult<T> getOne(String email);

    /**
     * 회원 정보 목록을 검색합니다.
     *
     * @return T 타입의 회원 정보 목록을 포함하는 infoResult를 반환합니다.
     */
    InfoResult<T> getList(Role role);

    /**
     * 회원 정보를 수정하고 결과를 반환합니다.
     *
     * @return 수정된 T 타입의 회원 정보를 포함하는 infoResult를 반환합니다.
     */
    InfoResult<T> edit(InfoUpdateReq req, String email);

    /**
     * 주어진 역할을 지원하는지 확인합니다.
     *
     * @param role 지원 여부를 확인할 역할입니다.
     * @return 역할이 지원되면 true, 그렇지 않으면 false를 반환합니다.
     */
    boolean supports(Role role);
}