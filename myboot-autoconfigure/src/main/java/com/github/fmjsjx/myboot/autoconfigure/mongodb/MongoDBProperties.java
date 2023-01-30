package com.github.fmjsjx.myboot.autoconfigure.mongodb;

import java.time.Duration;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.lang.NonNull;
import org.springframework.util.unit.DataSize;

import com.mongodb.AuthenticationMechanism;
import com.mongodb.MongoCompressor;
import com.mongodb.ServerAddress;
import com.mongodb.connection.ClusterConnectionMode;
import com.mongodb.connection.ClusterType;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Properties class for MongoDB.
 */
@Getter
@Setter
@ToString
@ConfigurationProperties(MongoDBProperties.CONFIG_PREFIX)
public class MongoDBProperties {

    static final String CONFIG_PREFIX = "myboot.mongodb";

    /**
     * The {@code MongoDB} clients.
     */
    private List<MongoClientProperties> clients;

    /**
     * MongoDB driver type.
     * 
     * @since 0.1
     *
     * @author MJ Fang
     */
    public enum DriverType {
        /**
         * {@code mongodb-driver-sync}.
         */
        SYNC,
        /**
         * {@code mongodb-driver-reactivestreams}.
         */
        REACTIVESTREAMS
    }

    /**
     * Properties class for {@code MongoClient}.
     */
    @Getter
    @Setter
    @ToString
    public static class MongoClientProperties {

        @NonNull
        private String name;
        /**
         * The default is <code>"${name}MongoClient"</code>.
         */
        private String beanName;
        /**
         * Weather this client is primary or not.
         */
        private boolean primary;
        /**
         * The default is {@code sync}.
         */
        private DriverType driver = DriverType.SYNC;
        /**
         * Weather this client will use {@code NETTY} stream or not.
         * <p>
         * The default is {@code true}.
         */
        private boolean useNetty = true;
        /**
         * The string value of MongoDB Connection String.
         */
        private String uri;
        /**
         * The host name from which to lookup SRV record for the seed list.
         * <p>
         * Can't be set with {@code uri}.
         */
        private String srvHost;
        /**
         * The seed list of hosts for the cluster.
         * <p>
         * Can't be set with {@code uri}.
         */
        private List<ServerHost> hosts;
        /**
         * The authentication mechanisms.
         * <p>
         * Can't be set with {@code uri}.
         */
        private AuthenticationMechanism authMechanism;
        /**
         * The user name.
         * <p>
         * Can't be set with {@code uri}.
         */
        private String username;
        /**
         * The password.
         * <p>
         * Can't be set with {@code uri}.
         */
        private char[] password;
        /**
         * The database where the user is defined.
         * <p>
         * The default is {@code "admin"}.
         * <p>
         * Can't be set with {@code uri}.
         */
        private String authdb;
        /**
         * The cluster connection mode.
         * <p>
         * Can't be set with {@code uri}.
         */
        private ClusterConnectionMode clusterConnectionMode;
        /**
         * The required replica set name.
         * <p>
         * Can't be set with {@code uri}.
         */
        private String requiredReplicaSetName;
        /**
         * The required cluster type.
         * <p>
         * Can't be set with {@code uri}.
         */
        private ClusterType requiredClusterType;
        /**
         * The local threshold.
         * <p>
         * The default is {@code 15ms}.
         * <p>
         * Can't be set with {@code uri}.
         */
        private Duration localThreshold;
        /**
         * The timeout to apply when selecting a server.
         * <p>
         * The default is {@code 30s}.
         * <p>
         * Can't be set with {@code uri}.
         */
        private Duration serverSelectionTimeout;
        /**
         * All settings that relate to the pool of connections to a MongoDB server.
         * <p>
         * Can't be set with {@code uri}.
         */
        @NestedConfigurationProperty
        private PoolProperties pool;
        /**
         * The frequency that the cluster monitor attempts to reach each server.
         * <p>
         * The default is {@code 10s}.
         * <p>
         * Can't be set with {@code uri}.
         */
        private Duration heartbeatFrequency;
        /**
         * The minimum heartbeat frequency.
         * <p>
         * The default is {@code 500ms}.
         * <p>
         * Can't be set with {@code uri}.
         */
        private Duration minHeartbeatFrequency;
        /**
         * All socket settings used for connections to a MongoDB server.
         * <p>
         * Can't be set with {@code uri}.
         */
        @NestedConfigurationProperty
        private SocketProperties socket;
        /**
         * All settings for connecting to MongoDB via SSL.
         * <p>
         * Can't be set with {@code uri}.
         */
        @NestedConfigurationProperty
        private SslProperties ssl;
        /**
         * The compressor list.
         */
        private List<CompressorProperties> compressorList;
        /**
         * The database list.
         */
        private List<DatabaseProperties> databases;

    }

    /**
     * Properties class for MongoDB server.
     */
    @Getter
    @Setter
    @ToString
    public static class ServerHost {

        /**
         * The hostname.
         */
        private String host;
        /**
         * The mongod port.
         */
        private Integer port;

        /**
         * Return a new {@link ServerAddress} instance.
         * 
         * @return a {@code ServerAddress}
         */
        ServerAddress toServerAddress() {
            return port == null ? new ServerAddress(host) : new ServerAddress(host, port);
        }

    }

    /**
     * Properties class for MongoDB connection pool.
     */
    @Getter
    @Setter
    @ToString
    public static class PoolProperties {

        /**
         * The maximum number of connections allowed.
         * <p>
         * The default is {@code 100}.
         */
        private Integer maxSize;
        /**
         * The minimum number of connections.
         * <p>
         * The default is {@code 0}.
         */
        private Integer minSize;
        /**
         * The maximum time that a thread may wait for a connection to become available.
         * <p>
         * The default is {@code 2m}.
         */
        private Duration maxWaitTime;
        /**
         * The maximum time a pooled connection can live for.
         */
        private Duration maxConnectionLifeTime;
        /**
         * The maximum idle time of a pooled connection.
         */
        private Duration maxConnectionIdleTime;
        /**
         * The period of time to wait before running the first maintenance job on the
         * connection pool.
         * 
         */
        private Duration maintenanceInitialDelay;
        /**
         * The time period between runs of the maintenance job.
         * <p>
         * The default is {@code 1m}.
         */
        private Duration maintenanceFrequency;

    }

    /**
     * Properties class for MongoDB connection socket.
     */
    @Getter
    @Setter
    @ToString
    public static class SocketProperties {
        /**
         * The socket connect timeout.
         * <p>
         * The default is {@code 10s}.
         */
        private Duration connectTimeout;
        /**
         * The socket read timeout.
         */
        private Duration readTimeout;
        /**
         * The receive buffer size.
         */
        private DataSize receiveBufferSize;
        /**
         * The send buffer size.
         */
        private DataSize sendBufferSize;
    }

    /**
     * Properties class for MongoDB connection SSL.
     */
    @Getter
    @Setter
    @ToString
    public static class SslProperties {

        /**
         * Define whether SSL should be enabled.
         * <p>
         * The default is {@code false}.
         */
        private Boolean enabled;
        /**
         * Define whether invalid host names should be allowed.
         * <p>
         * The default is {@code false}.
         */
        private Boolean invalidHostNameAllowed;
    }

    /**
     * Properties class for MongoDB compressor.
     */
    @Getter
    @Setter
    @ToString
    public static class CompressorProperties {

        /**
         * The compression algorithm.
         */
        @NonNull
        private CompressionAlgorithm algorithm;
        /**
         * The compression level.
         */
        private Integer level;

        /**
         * Returns a new {@link MongoCompressor} instance.
         * 
         * @return a {@code MongoCompressor}.
         */
        MongoCompressor toMongoCompressor() {
            MongoCompressor compressor = switch (algorithm) {
                case SNAPPY -> MongoCompressor.createSnappyCompressor();
                case ZLIB -> MongoCompressor.createZlibCompressor();
                case ZSTD -> MongoCompressor.createZstdCompressor();
            };
            if (level != null) {
                compressor = compressor.withProperty(MongoCompressor.LEVEL, level);
            }
            return compressor;
        }

    }

    /**
     * MongoDB compression algorithms.
     */
    public enum CompressionAlgorithm {
        /**
         * {@code snappy}.
         */
        SNAPPY,
        /**
         * {@code zlib}.
         */
        ZLIB,
        /**
         * {@code zstd}.
         */
        ZSTD
    }

    /**
     * Configuration properties for {@code MongoDatabse}s.
     */
    @Getter
    @Setter
    @ToString
    public static class DatabaseProperties {
        /**
         * The name of the database.
         */
        @NonNull
        private String name;
        /**
         * The id of the {@code MongoDatabase} instance.
         */
        @NonNull
        private String id;
        /**
         * The default is <code>"${id}MongoDatabase"</code>;
         */
        private String beanName;
        /**
         * Weather is {@code MongoDatabase} is primary or not.
         * <p>
         * Value {@code true} only effect when this database belongs to a primary
         * {@code MongoClient}.
         */
        private boolean primary;

    }

}
