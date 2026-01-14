package com.github.fmjsjx.myboot.r2dbc;

import com.github.fmjsjx.libcommon.util.StringUtil;
import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactoryOptions;
import io.r2dbc.spi.Option;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.boot.r2dbc.autoconfigure.R2dbcProperties;

import java.util.Objects;

/**
 * A static factory to creates {@link ConnectionPool}s.
 *
 * @author MJ Fang
 * @since 4.1
 */
public class ConnectionPoolFactory {

    /**
     * Creates a new {@link ConnectionPool} instance by the specified
     * {@link R2dbcProperties} given.
     *
     * @param properties the R2DBC configuration properties
     * @return a new {@link ConnectionPool} instance
     */
    @NonNull
    public static final ConnectionPool create(@NonNull R2dbcProperties properties) {
        var urlOptions = ConnectionFactoryOptions.parse(Objects.requireNonNull(properties.getUrl(), "url must not be null"));
        var optionsBuilder = urlOptions.mutate();
        if (!urlOptions.hasOption(ConnectionFactoryOptions.USER) && StringUtil.isNotBlank(properties.getUsername())) {
            optionsBuilder.option(ConnectionFactoryOptions.USER, properties.getUsername());
        }
        if (!urlOptions.hasOption(ConnectionFactoryOptions.PASSWORD) && StringUtil.isNotBlank(properties.getPassword())) {
            optionsBuilder.option(ConnectionFactoryOptions.PASSWORD, properties.getPassword());
        }
        if (!urlOptions.hasOption(ConnectionFactoryOptions.DATABASE)) {
            var database = properties.isGenerateUniqueName() ? properties.determineUniqueName() : properties.getName();
            if (StringUtil.isNotBlank(database)) {
                optionsBuilder.option(ConnectionFactoryOptions.DATABASE, database);
            }
        }
        properties.getProperties().forEach((key, value) -> optionsBuilder.option(Option.valueOf(key), value));
        var builder = ConnectionPoolConfiguration.builder(ConnectionFactories.get(optionsBuilder.build()));
        var pool = properties.getPool();
        if (pool.isEnabled()) {
            var map = PropertyMapper.get();
            map.from(pool.getMaxIdleTime()).to(builder::maxIdleTime);
            map.from(pool.getMaxLifeTime()).to(builder::maxLifeTime);
            map.from(pool.getMaxAcquireTime()).to(builder::maxAcquireTime);
            map.from(pool.getAcquireRetry()).to(builder::acquireRetry);
            map.from(pool.getMaxCreateConnectionTime()).to(builder::maxCreateConnectionTime);
            map.from(pool.getInitialSize()).to(builder::initialSize);
            map.from(pool.getMaxSize()).to(builder::maxSize);
            map.from(pool.getValidationQuery()).whenHasText().to(builder::validationQuery);
            map.from(pool.getValidationDepth()).to(builder::validationDepth);
            map.from(pool.getMinIdle()).to(builder::minIdle);
            map.from(pool.getMaxValidationTime()).to(builder::maxValidationTime);
        }
        return new ConnectionPool(builder.build());

    }

    private ConnectionPoolFactory() {
    }

}
