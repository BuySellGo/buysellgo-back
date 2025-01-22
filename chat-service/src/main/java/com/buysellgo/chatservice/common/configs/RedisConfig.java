package com.buysellgo.chatservice.common.configs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@Slf4j
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    private static final String REDIS_HOST_LOG_MESSAGE = "암호화 된 redis의 host 값: {}";

    // 기본 RedisConnectionFactory 추가
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        log.info(REDIS_HOST_LOG_MESSAGE, host);
        configuration.setHostName(host);
        configuration.setPort(port);
        configuration.setDatabase(0); // 0번 DB 사용
        return new LettuceConnectionFactory(configuration);
    }

    // 기본 RedisTemplate 추가
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        return createRedisTemplate(redisConnectionFactory());
    }

    // 사용자(User)용 RedisConnectionFactory
    @Bean(name = "userRedisFactory")
    public RedisConnectionFactory userRedisConnectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        log.info(REDIS_HOST_LOG_MESSAGE, host);
        configuration.setHostName(host);
        configuration.setPort(port);
        configuration.setDatabase(1); // 1번 DB를 사용
        return new LettuceConnectionFactory(configuration);
    }

    // 셀러(Seller)용 RedisConnectionFactory
    @Bean(name = "sellerRedisFactory")
    public RedisConnectionFactory sellerRedisConnectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        log.info(REDIS_HOST_LOG_MESSAGE, host);
        configuration.setHostName(host);
        configuration.setPort(port);
        configuration.setDatabase(2); // 2번 DB를 사용
        return new LettuceConnectionFactory(configuration);
    }

    // 관리자(admin)용 RedisConnectionFactory
    @Bean(name = "adminRedisFactory")
    public RedisConnectionFactory adminRedisConnectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        log.info(REDIS_HOST_LOG_MESSAGE, host);
        configuration.setHostName(host);
        configuration.setPort(port);
        configuration.setDatabase(3); // 2번 DB를 사용
        return new LettuceConnectionFactory(configuration);
    }

    @Bean(name = "verificationRedisFactory")
    public RedisConnectionFactory verificationRedisConnectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(host);
        configuration.setPort(port);
        configuration.setDatabase(4); // 4번 DB를 사용
        return new LettuceConnectionFactory(configuration);
    }

    @Bean(name = "userTemplate")
    public RedisTemplate<String, Object> userRedisTemplate(
            @Qualifier("userRedisFactory") RedisConnectionFactory factory) {
        return createRedisTemplate(factory);
    }

    @Bean(name = "sellerTemplate")
    public RedisTemplate<String, Object> sellerRedisTemplate(
            @Qualifier("sellerRedisFactory") RedisConnectionFactory factory) {
        return createRedisTemplate(factory);
    }

    @Bean(name = "adminTemplate")
    public RedisTemplate<String, Object> adminRedisTemplate(
            @Qualifier("adminRedisFactory") RedisConnectionFactory factory) {
        return createRedisTemplate(factory);
    }

    @Bean(name = "verificationTemplate")
    public RedisTemplate<String, Object> verificationTemplate(
            @Qualifier("verificationRedisFactory") RedisConnectionFactory factory) {
        return createRedisTemplate(factory);
    }

    private RedisTemplate<String, Object> createRedisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setConnectionFactory(factory);
        return template;
    }
}












