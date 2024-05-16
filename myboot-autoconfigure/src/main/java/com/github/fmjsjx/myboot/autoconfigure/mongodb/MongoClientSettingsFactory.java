package com.github.fmjsjx.myboot.autoconfigure.mongodb;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.github.fmjsjx.myboot.autoconfigure.mongodb.MongoDBProperties.CompressorProperties;
import com.github.fmjsjx.myboot.autoconfigure.mongodb.MongoDBProperties.MongoClientProperties;
import com.github.fmjsjx.myboot.autoconfigure.mongodb.MongoDBProperties.PoolProperties;
import com.github.fmjsjx.myboot.autoconfigure.mongodb.MongoDBProperties.ServerHost;
import com.github.fmjsjx.myboot.autoconfigure.mongodb.MongoDBProperties.SocketProperties;
import com.github.fmjsjx.myboot.autoconfigure.mongodb.MongoDBProperties.SslProperties;
import com.mongodb.AuthenticationMechanism;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.connection.*;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class MongoClientSettingsFactory {

    static MongoClientSettings create(MongoClientProperties config) {
        var builder = MongoClientSettings.builder();
        // application name
        Optional.ofNullable(config.getApplicationName()).ifPresent(builder::applicationName);
        // compressor list
        Optional.ofNullable(config.getCompressorList()).filter(l -> l.size() > 0).ifPresent(list -> {
            var cl = list.stream().map(CompressorProperties::toMongoCompressor).collect(Collectors.toList());
            log.debug("Set compressor list >>> {}", cl);
            builder.compressorList(cl);
        });
        // uuid representation
        Optional.ofNullable(config.getUuidRepresentation()).ifPresent(builder::uuidRepresentation);
        builder.applyToClusterSettings(b -> apply(b, config)) // cluster
                .applyToConnectionPoolSettings(b -> apply(b, config.getPool())) // pool
                .applyToServerSettings(b -> apply(b, config)) // server
                .applyToSocketSettings(b -> apply(b, config.getSocket())) // socket
                .applyToSslSettings(b -> apply(b, config.getSsl())); // SSL
        // credential
        var mechanism = config.getAuthMechanism();
        var userName = config.getUsername();
        var source = Optional.ofNullable(config.getAuthdb()).orElse("admin");
        if (userName != null || mechanism != null) {
            builder.credential(createCredential(mechanism, userName, source, config.getPassword()));
        }
        // uri
        Optional.ofNullable(config.getUri()).map(ConnectionString::new).ifPresent(builder::applyConnectionString);
        if (config.isUseNetty()) {
            var library = MongoDBAutoConfiguration.getNettyLibrary();
            var transportSettings = TransportSettings.nettyBuilder().eventLoopGroup(library.getEventLoopGroup())
                    .socketChannelClass(library.getSocketChannelClass()).build();
            log.debug("Set MongoClient NettyTransportSettings >>> {}", transportSettings);
            builder.transportSettings(transportSettings);
        }
        return builder.build();
    }

    private static void apply(ClusterSettings.Builder builder, MongoClientProperties config) {
        Optional.ofNullable(config.getSrvHost()).ifPresent(builder::srvHost);
        Optional.ofNullable(config.getHosts())
                .map(hosts -> hosts.stream().map(ServerHost::toServerAddress).collect(Collectors.toList()))
                .ifPresent(builder::hosts);
        Optional.ofNullable(config.getClusterConnectionMode()).ifPresent(builder::mode);
        Optional.ofNullable(config.getRequiredReplicaSetName()).ifPresent(builder::requiredReplicaSetName);
        Optional.ofNullable(config.getRequiredClusterType()).ifPresent(builder::requiredClusterType);
        Optional.ofNullable(config.getLocalThreshold())
                .ifPresent(v -> builder.localThreshold(v.toMillis(), TimeUnit.MILLISECONDS));
        Optional.ofNullable(config.getServerSelectionTimeout())
                .ifPresent(v -> builder.serverSelectionTimeout(v.toMillis(), TimeUnit.MILLISECONDS));
    }

    private static void apply(ConnectionPoolSettings.Builder builder, PoolProperties config) {
        if (config != null) {
            Optional.ofNullable(config.getMaxSize()).ifPresent(builder::maxSize);
            Optional.ofNullable(config.getMinSize()).ifPresent(builder::minSize);
            Optional.ofNullable(config.getMaxWaitTime())
                    .ifPresent(v -> builder.maxWaitTime(v.toMillis(), TimeUnit.MILLISECONDS));
            Optional.ofNullable(config.getMaxConnectionLifeTime())
                    .ifPresent(v -> builder.maxConnectionLifeTime(v.toMillis(), TimeUnit.MILLISECONDS));
            Optional.ofNullable(config.getMaxConnectionIdleTime())
                    .ifPresent(v -> builder.maxConnectionIdleTime(v.toMillis(), TimeUnit.MILLISECONDS));
            Optional.ofNullable(config.getMaintenanceInitialDelay())
                    .ifPresent(v -> builder.maintenanceInitialDelay(v.toMillis(), TimeUnit.MILLISECONDS));
            Optional.ofNullable(config.getMaintenanceFrequency())
                    .ifPresent(v -> builder.maintenanceFrequency(v.toMillis(), TimeUnit.MILLISECONDS));
        }
    }

    private static void apply(ServerSettings.Builder builder, MongoClientProperties config) {
        Optional.ofNullable(config.getHeartbeatFrequency())
                .ifPresent(v -> builder.heartbeatFrequency(v.toMillis(), TimeUnit.MILLISECONDS));
        Optional.ofNullable(config.getMinHeartbeatFrequency())
                .ifPresent(v -> builder.minHeartbeatFrequency(v.toMillis(), TimeUnit.MILLISECONDS));
    }

    private static void apply(SocketSettings.Builder builder, SocketProperties config) {
        if (config != null) {
            Optional.ofNullable(config.getConnectTimeout())
                    .ifPresent(v -> builder.connectTimeout((int) v.toMillis(), TimeUnit.MILLISECONDS));
            Optional.ofNullable(config.getReadTimeout())
                    .ifPresent(v -> builder.readTimeout((int) v.toMillis(), TimeUnit.MILLISECONDS));
            Optional.ofNullable(config.getReceiveBufferSize())
                    .ifPresent(v -> builder.receiveBufferSize((int) v.toBytes()));
            Optional.ofNullable(config.getSendBufferSize()).ifPresent(v -> builder.sendBufferSize((int) v.toBytes()));
        }
    }

    private static void apply(SslSettings.Builder builder, SslProperties config) {
        if (config != null) {
            Optional.ofNullable(config.getEnabled()).ifPresent(builder::enabled);
            Optional.ofNullable(config.getInvalidHostNameAllowed()).ifPresent(builder::invalidHostNameAllowed);
        }

    }

    @SuppressWarnings("UnnecessaryDefault")
    private static MongoCredential createCredential(AuthenticationMechanism mechanism, String userName, String source,
                                                    char[] password) {
        if (mechanism != null) {
            return switch (mechanism) {
                case GSSAPI -> MongoCredential.createGSSAPICredential(userName);
                case MONGODB_X509 -> MongoCredential.createMongoX509Credential(userName);
                case PLAIN -> MongoCredential.createPlainCredential(userName, source, password);
                case SCRAM_SHA_1 -> MongoCredential.createScramSha1Credential(userName, source, password);
                case SCRAM_SHA_256 -> MongoCredential.createScramSha256Credential(userName, source, password);
                case MONGODB_AWS -> MongoCredential.createAwsCredential(userName, password);
                case MONGODB_OIDC -> MongoCredential.createOidcCredential(userName);
                default -> throw new UnsupportedOperationException("unsupported mechanism " + mechanism);
            };
        }
        return MongoCredential.createCredential(userName, source, password);
    }

}
