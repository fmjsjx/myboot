package com.github.fmjsjx.myboot.example.mongodb;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.mongodb.client.MongoDatabase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

/**
 * MongoDB manager.
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Slf4j
@Service
public class MongoDBManager implements InitializingBean {

    private final MongoDatabase syncTestMongoDatabase;

    private final com.mongodb.reactivestreams.client.MongoDatabase reactivestreamsTestMongoDatabase;

    /**
     * Constructs {@link MongoDBManager}.
     *
     * @param syncTestMongoDatabase            the syncTestMongoDatabase
     * @param reactivestreamsTestMongoDatabase the reactivestreamsTestMongoDatabase
     */
    public MongoDBManager(@Qualifier("syncTestMongoDatabase") MongoDatabase syncTestMongoDatabase,
                          @Qualifier("reactivestreamsTestMongoDatabase") com.mongodb.reactivestreams.client.MongoDatabase reactivestreamsTestMongoDatabase) {
        this.syncTestMongoDatabase = syncTestMongoDatabase;
        this.reactivestreamsTestMongoDatabase = reactivestreamsTestMongoDatabase;
    }


    @Override
    public void afterPropertiesSet() {
        log.debug("Sync Test: {}", syncTestMongoDatabase);
        var names = StreamSupport.stream(syncTestMongoDatabase.listCollectionNames().spliterator(), false)
                .collect(Collectors.toList());
        log.debug("Collection names: {}", names);
        log.debug("Reactive Streams Test: {}", reactivestreamsTestMongoDatabase);
        names = Flux.from(reactivestreamsTestMongoDatabase.listCollectionNames()).collectList().block();
        log.debug("Collection names: {}", names);
    }

}
