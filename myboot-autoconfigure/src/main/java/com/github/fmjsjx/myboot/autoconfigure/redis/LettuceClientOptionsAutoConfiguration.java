package com.github.fmjsjx.myboot.autoconfigure.redis;

import io.lettuce.core.ClientOptions;
import io.lettuce.core.cluster.ClusterClientOptions;
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

    @Bean
    @ConditionalOnMissingBean
    public ClientOptions clientOptions() {
        return ClientOptions.create();
    }

    @Bean
    @ConditionalOnMissingBean
    public ClusterClientOptions clusterClientOptions() {
        return ClusterClientOptions.create();
    }

}
