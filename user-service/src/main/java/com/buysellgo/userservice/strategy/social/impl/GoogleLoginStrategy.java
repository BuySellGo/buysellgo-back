package com.buysellgo.userservice.strategy.social.impl;

import com.buysellgo.userservice.common.configs.SocialLoginProperties;
import com.buysellgo.userservice.strategy.social.common.SocialLoginResult;
import com.buysellgo.userservice.strategy.social.common.SocialLoginStrategy;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;
    
@Component
@Transactional  
@RequiredArgsConstructor
class GoogleLoginStrategy implements SocialLoginStrategy<Map<String, Object>> {
    private final SocialLoginProperties socialLoginProperties;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public SocialLoginResult<Map<String, Object>> execute(String code) {
        MultiValueMap<String, String> tokenParams = createTokenParams(code);
        SocialLoginResult<Map<String, Object>> tokenResult = requestAccessToken(tokenParams);
        if (!tokenResult.success()) {
            return tokenResult;
        }

        String accessToken = extractAccessToken(tokenResult);
        return requestUserInfo(accessToken);
    }

    @Override
    public MultiValueMap<String, String> createTokenParams(String code) {
        MultiValueMap<String, String> tokenParams = new LinkedMultiValueMap<>();
        tokenParams.add("grant_type", "authorization_code");
        tokenParams.add("client_id", socialLoginProperties.getGoogleClientId());
        tokenParams.add("client_secret", socialLoginProperties.getGoogleClientSecret());
        tokenParams.add("redirect_uri", socialLoginProperties.getGoogleCallbackUrl());
        tokenParams.add("code", code);
        return tokenParams;
    }

    @Override
    public SocialLoginResult<Map<String, Object>> requestAccessToken(MultiValueMap<String, String> tokenParams) {
        Map<String, Object> data = new HashMap<>();
        HttpHeaders tokenHeaders = new HttpHeaders();
        tokenHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> tokenRequest = new HttpEntity<>(tokenParams, tokenHeaders);
        ResponseEntity<String> tokenResponse;
        try {
            tokenResponse = restTemplate.postForEntity("https://oauth2.googleapis.com/token", tokenRequest, String.class);
        } catch (Exception e) {
            return SocialLoginResult.fail("Failed to retrieve access token due to network error", data);
        }

        JsonNode tokenJson;
        try {
            tokenJson = objectMapper.readTree(tokenResponse.getBody());
        } catch (JsonProcessingException e) {
            return SocialLoginResult.fail("Failed to parse access token response", data);
        }

        String accessToken = tokenJson.path("access_token").asText();
        if (accessToken == null || accessToken.isEmpty()) {
            return SocialLoginResult.fail("Failed to retrieve access token", data);
        }

        data.put("accessToken", accessToken);
        return SocialLoginResult.success("Access token retrieved successfully", data);
    }

    @Override
    public String extractAccessToken(SocialLoginResult<Map<String, Object>> tokenResult) {
        return (String) tokenResult.data().get("accessToken");
    }

    @Override
    public SocialLoginResult<Map<String, Object>> requestUserInfo(String accessToken) {
        Map<String, Object> data = new HashMap<>();
        HttpHeaders userInfoHeaders = new HttpHeaders();
        userInfoHeaders.setBearerAuth(accessToken);

        HttpEntity<String> userInfoRequest = new HttpEntity<>(null, userInfoHeaders);
        ResponseEntity<String> userInfoResponse;
        try {
            userInfoResponse = restTemplate.exchange("https://openidconnect.googleapis.com/v1/userinfo", HttpMethod.GET, userInfoRequest, String.class);
        } catch (Exception e) {
            return SocialLoginResult.fail("Failed to retrieve user info due to network error", data);
        }

        try {
            JsonNode userInfoJson = objectMapper.readTree(userInfoResponse.getBody());
            String email = userInfoJson.path("email").asText(null);

            if (email.isEmpty()) {
                return SocialLoginResult.fail("Email not found in user info", data);
            }
            data.put("email", email);
        } catch (JsonProcessingException e) {
            return SocialLoginResult.fail("Failed to parse user info response", data);
        }

        return SocialLoginResult.success("User info retrieved successfully", data);
    }

    @Override
    public Boolean support(String provider) {
        return "google".equals(provider);
    }
}
