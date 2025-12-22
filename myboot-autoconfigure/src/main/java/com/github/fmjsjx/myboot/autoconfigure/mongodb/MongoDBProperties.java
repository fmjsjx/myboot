package com.github.fmjsjx.myboot.autoconfigure.mongodb;

import java.time.Duration;
import java.util.List;

import com.mongodb.AuthenticationMechanism;
import com.mongodb.MongoCompressor;
import com.mongodb.ServerAddress;
import com.mongodb.connection.ClusterConnectionMode;
import com.mongodb.connection.ClusterType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bson.UuidRepresentation;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.util.unit.DataSize;

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
     * @author MJ Fang
     * @since 0.1
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
         */
        private String srvHost;
        /**
         * The seed list of hosts for the cluster.
         */
        private List<ServerHost> hosts;
        /**
         * The authentication mechanisms.
         */
        private AuthenticationMechanism authMechanism;
        /**
         * The user name.
         */
        private String username;
        /**
         * The password.
         */
        private char[] password;
        /**
         * The database where the user is defined.
         * <p>
         * The default is {@code "admin"}.
         */
        private String authdb;
        /**
         * The cluster connection mode.
         */
        private ClusterConnectionMode clusterConnectionMode;
        /**
         * The required replica set name.
         */
        private String requiredReplicaSetName;
        /**
         * The required cluster type.
         */
        private ClusterType requiredClusterType;
        /**
         * The local threshold.
         * <p>
         * The default is {@code 15ms}.
         */
        private Duration localThreshold;
        /**
         * The timeout to apply when selecting a server.
         * <p>
         * The default is {@code 30s}.
         * <p>
         */
        private Duration serverSelectionTimeout;
        /**
         * All settings that relate to the pool of connections to a MongoDB server.
         */
        @NestedConfigurationProperty
        private PoolProperties pool;
        /**
         * The frequency that the cluster monitor attempts to reach each server.
         * <p>
         * The default is {@code 10s}.
         */
        private Duration heartbeatFrequency;
        /**
         * The minimum heartbeat frequency.
         * <p>
         * The default is {@code 500ms}.
         */
        private Duration minHeartbeatFrequency;
        /**
         * All socket settings used for connections to a MongoDB server.
         */
        @NestedConfigurationProperty
        private SocketProperties socket;
        /**
         * All settings for connecting to MongoDB via SSL.
         */
        @NestedConfigurationProperty
        private SslProperties ssl;
        /**
         * The logical name of the application. The application name may be used by the client to identify the
         * application to the server, for use in server logs, slow query logs, and profile collection.
         *
         * @since 2.x
         */
        private String applicationName;
        /**
         * The UUID representation to use when encoding instances of {@link java.util.UUID} and when decoding BSON
         * binary values with subtype of 3.
         * <p>
         * The default is {@link UuidRepresentation#UNSPECIFIED}, If your application stores UUID values in MongoDB,
         * you must set this value to the desired representation. New applications should prefer
         * {@link UuidRepresentation#STANDARD}, while existing Java applications should prefer
         * {@link UuidRepresentation#JAVA_LEGACY}. Applications wishing to interoperate with existing Python or .NET
         * applications should prefer {@link UuidRepresentation#PYTHON_LEGACY} or
         * {@link UuidRepresentation#C_SHARP_LEGACY}, respectively. Applications that do not store UUID values in
         * MongoDB don't need to set this value.
         *
         * @since 2.x
         */
        private UuidRepresentation uuidRepresentation;
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
