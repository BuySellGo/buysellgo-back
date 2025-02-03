package com.buysellgo.productservice.common.util;


public enum CommonConstant {


    // 메시지 상수
    USER_NOT_FOUND("사용자를 찾을 수 없습니다."),
    TOKEN_OR_USER_NOT_FOUND("리프레시 토큰이 만료되었거나, 해당 사용자가 존재하지 않습니다."),
    PASSWORD_NOT_MATCHED("패스워드가 일치하지 않습니다."),
    REFRESH_TOKEN_DELETED("리프레시 토큰이 삭제되었습니다."),
    DTO_NOT_MATCHED("해당 dto를 지원하지 않습니다."),
    SELLER_CREATED("판매자 가입완료"),
    USER_CREATED("회원 가입완료"),
    USER_DELETED("회원 삭제완료"),
    SELLER_DELETED("판매자 삭제완료"),
    VALUE_DUPLICATED("중복된 값이 있습니다."),
    SAVE_FAILURE("저장 실패"),
    ROLE_NOT_MATCHED("해당 역할이 아닙니다."),
    EMAIL_DUPLICATED("이메일이 중복됩니다."),
    USERNAME_DUPLICATED("사용자명이 중복됩니다."),
    COMPANY_NAME_DUPLICATED("회사명이 중복됩니다."),
    NO_DUPLICATION("중복된 값이 없습니다."),
    SELLER_ACTIVATED("판매자 승인 완료"),
    USER_ACTIVATED("회원 활성화 완료"),
    USER_DEACTIVATED("회원 비활성화 완료"),
    SELLER_DEACTIVATED("판매자 비활성화 완료"),
    ADMIN_SIGN_UP_SUCCESS("관리자 회원가입 완료"),
    TYPE_NOT_SUPPORTED("지원하지 않는 로그인 제공자입니다."),
    NO_CHANGE("업데이트할 값이 없습니다."),
    UPDATE_SUCCESS("업데이트 성공"),
    INVALID_ROLE("유효하지 않은 역할입니다."),
    INVALID_TOKEN("잘못된 Authorization 헤더 형식입니다."),
    REVIEW_CREATED("리뷰 작성 완료"),
    REVIEW_UPDATED("리뷰 수정 완료"),
    REVIEW_DELETED("리뷰 삭제 완료"),
    REVIEW_ACTIVE("리뷰 활성화 완료"),
    REVIEW_INACTIVE("리뷰 비활성화 완료"),
    REVIEW_NOT_FOUND("리뷰를 찾을 수 없습니다."),
    REVIEW_NOT_WRITABLE("리뷰 작성 권한이 없습니다."),
    REVIEW_NOT_UPDATABLE("리뷰 수정 권한이 없습니다."),
    REVIEW_NOT_DELETABLE("리뷰 삭제 권한이 없습니다."),
    REVIEW_NOT_ACTIVABLE("리뷰 활성화 권한이 없습니다."),
    REVIEW_NOT_INACTIVABLE("리뷰 비활성화 권한이 없습니다."),
    UPDATE_FAILURE("업데이트 실패"),
    DELETE_FAILURE("삭제 실패"),
    ACTIVE_FAILURE("활성화 실패"),
    INACTIVE_FAILURE("비활성화 실패"),
    GET_FAILURE("조회 실패"),
    WRITE_FAILURE("작성 실패"),
    REVIEW_VO("reviewVo"),
    REVIEW_DELETE_SUCCESS("리뷰 삭제 완료"),
    REVIEW_WRITE_SUCCESS("리뷰 작성 완료"),
    REVIEW_UPDATE_SUCCESS("리뷰 수정 완료"),
    REVIEW_GET_SUCCESS("리뷰 조회 완료"),
    REVIEW_WRITE_FAIL("리뷰 작성 실패"),
    REVIEW_UPDATE_FAIL("리뷰 수정 실패"),
    REVIEW_DELETE_FAIL("리뷰 삭제 실패"),
    REVIEW_DELETE_PERMISSION_DENIED("리뷰 삭제 권한이 없습니다."),
    REVIEW_WRITE_PERMISSION_DENIED("리뷰 작성 권한이 없습니다."),
    REVIEW_UPDATE_PERMISSION_DENIED("리뷰 수정 권한이 없습니다."),
    QNA_CREATE_SUCCESS("Qna 작성 완료"),
    QNA_CREATE_FAIL("Qna 작성 실패"),
    QNA_LIST_SUCCESS("Qna 조회 완료"),
    QNA_LIST_FAIL("Qna 조회 실패"),
    QNA_NOT_FOUND("Qna를 찾을 수 없습니다."),
    REPLY_CREATE_SUCCESS("답변 작성 완료"),
    REPLY_CREATE_FAIL("답변 작성 실패"),
    REPLY_UPDATE_SUCCESS("답변 수정 완료"),
    REPLY_UPDATE_FAIL("답변 수정 실패"),
    REPLY_DELETE_SUCCESS("답변 삭제 완료"),
    REPLY_DELETE_FAIL("답변 삭제 실패"),
    REPLY_ALREADY_EXISTS("이미 답변이 있습니다."),
    REPLY_NOT_FOUND("답변을 찾을 수 없습니다."),
    QNA_DELETE_SUCCESS("Qna 삭제 완료"),
    QNA_DELETE_FAIL("Qna 삭제 실패"),
    QNA_UPDATE_SUCCESS("Qna 수정 완료"),
    QNA_UPDATE_FAIL("Qna 수정 실패"),
    QNA_DELETE_PERMISSION_DENIED("Qna 삭제 권한이 없습니다."),
    QNA_UPDATE_PERMISSION_DENIED("Qna 수정 권한이 없습니다."),
    REPLY_UPDATE_PERMISSION_DENIED("답변 수정 권한이 없습니다."),
    REPLY_DELETE_PERMISSION_DENIED("답변 삭제 권한이 없습니다."),
    NOT_SUPPORTED("지원하지 않는 기능입니다."),
    PRODUCT_CREATE_SUCCESS("상품 생성 완료"),
    PRODUCT_CREATE_FAIL("상품 생성 실패"),
    PRODUCT_UPDATE_SUCCESS("상품 수정 완료"),
    PRODUCT_UPDATE_FAIL("상품 수정 실패"),
    PRODUCT_DELETE_SUCCESS("상품 삭제 완료"),
    PRODUCT_DELETE_FAIL("상품 삭제 실패"),
    PRODUCT_DUPLICATED("상품 이름이 중복됩니다."),
    PRODUCT_NOT_FOUND("상품을 찾을 수 없습니다."),
    PRODUCT_UPDATE_PERMISSION_DENIED("상품 수정 권한이 없습니다."),
    PRODUCT_DELETE_PERMISSION_DENIED("상품 삭제 권한이 없습니다."),
    PRODUCT_LIST_SUCCESS("상품 목록 조회 완료"),
    PRODUCT_LIST_FAIL("상품 목록 조회 실패"),
    PRODUCT_DETAIL_SUCCESS("상품 상세 조회 완료"),
    PRODUCT_DETAIL_FAIL("상품 상세 조회 실패"),
    PRODUCT_LIST_EMPTY("상품 목록이 없습니다."),
    // 토큰 상수

    BEARER_PREFIX("Bearer "),




    ACCESS_TOKEN("accessToken"),
    

    // 응답 키값 상수
    EMAIL("email"),
    ROLE("role"),
    USER_VO("userVo"),
    SELLER_VO("sellerVo"),
    ADMIN_VO("adminVo"),
    PROFILE_VO("profileVo"),
    QNA_VO("qnaVo"),
    REPLY_VO("replyVo"),
    PRODUCT_VO("productVo"),
    SUCCESS("success"),
    FAILURE("failure"),



    // 숫자 상수
    KEEP_LOGIN_HOURS(168L),
    DEFAULT_HOURS(10L);
    private final Object value;

    <T> CommonConstant(T value) {
        this.value = value;
    }



    @SuppressWarnings("unchecked")
    public <T> T getValue() {
        return (T) value;
    }


}
