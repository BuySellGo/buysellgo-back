package com.buysellgo.chatservice.common.configs;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableEncryptableProperties
public class JasyptConfig {

    @Bean("jasyptEncrypter")
    public StringEncryptor getStringEncryptor() {
        // 암호화를 진행하는 핵심 객체
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        // 핵심 객체의 설정값을 객체 형태로 한번에 모아서 전달할 수 있게 하는 객체
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();

        config.setAlgorithm("PBEWITHHMACSHA512ANDAES_256"); // 암호화 알고리즘
        config.setPassword("abc1234"); // 암/복호화에 사용되는 비밀번호를 설정. (yml에서 가져오는 것을 추천)
        config.setKeyObtentionIterations("1000"); // 암호화 해싱 횟수
        config.setPoolSize("1"); // 스레드 풀의 크기 (값을 크게 주면 암호화 성능 향상)
        config.setProviderName("SunJCE"); // 암호화 제공자
        config.setStringOutputType("base64"); // 암호화 된 문자열의 출력 형식
        // Salt: 암호화된 데이터를 보호하기 위해 암호화 과정에서 추가되는 랜덤값
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        // IV(Initialization Vector): 암호화 과정에서 초깃값으로 사용.
        config.setIvGeneratorClassName("org.jasypt.iv.RandomIvGenerator");
        encryptor.setConfig(config); // 세팅한 설정을 한번에 객체로 전달

        // 핵심 객체 빈 등록
        return encryptor;
    }

}

















