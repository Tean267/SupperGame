package com.tean.supergame.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfig {
    @Value("${spring.data.redis.host}")
    private String host;
    @Value("${spring.data.redis.password}")
    private String password;
    @Value("${spring.data.redis.port}")
    private int port;
    @Value("${spring.data.redis.timeout}")
    private int timeOut;
    @Value("${spring.data.redis.db}")
    private int db;

    @Bean
    public JedisPool create() {
        String password = "teandz123";
        String host = "localhost";
        int port = 6379;
        int timeOut = 60000;
        int db = 0;
        JedisPoolConfig config = new JedisPoolConfig();
        config.setJmxEnabled(false);
        config.setMaxTotal(30);
        config.setMaxIdle(30);

        if (password == null) {
            password = null;
        }

        return new JedisPool(config, host, port, timeOut, password, db);
    }

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(new RedisStandaloneConfiguration("localhost", 6379));
    }
}
