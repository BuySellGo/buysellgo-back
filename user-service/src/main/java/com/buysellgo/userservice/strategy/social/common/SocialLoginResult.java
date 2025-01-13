package com.buysellgo.userservice.strategy.social.common;


public record SocialLoginResult<T>(
    boolean success, 
    String message, 
    T data) {
     public static <T> SocialLoginResult<T> success(String message, T data) {
        return new SocialLoginResult<>(true, message,data);
    }

    public static <T> SocialLoginResult<T> fail(String message, T data) {
        return new SocialLoginResult<>(false, message,data);
    }
}
