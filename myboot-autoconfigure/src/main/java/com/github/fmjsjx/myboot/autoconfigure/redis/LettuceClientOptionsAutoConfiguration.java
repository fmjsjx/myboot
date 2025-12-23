package com.github.fmjsjx.myboot.autoconfigure.redis;

import io.lettuce.core.ClientOptions;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * Auto-Configuration class for {@code REDIS/Lettuce} {@link ClientOptions}.
 *
 * @author MJ Fang
 * @since 3.7
 */
@AutoConfiguration
@ConditionalOnClass(ClientOptions.class)
public class LettuceClientOptionsAutoConfiguration {

    /**
     * Constructs a new {@link LettuceClientOptionsAutoConfiguration}
     * instance.
     */
    public LettuceClientOptionsAutoConfiguration() {
    }

    /**
     * Bean definition function for {@link RedisClientConfigurer}.
     *
     * @return the {@link RedisClientConfigurer} bean
     */
    @Bean
    @ConditionalOnMissingBean
    public RedisClientConfigurer redisClientConfigurer() {
        return new RedisClientConfigurer() {
            // Anonymous inner class using only default methods.
        };
    }

}
