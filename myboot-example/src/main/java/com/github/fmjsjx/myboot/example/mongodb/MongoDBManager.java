package com.github.fmjsjx.myboot.example.mongodb;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.mongodb.client.MongoDatabase;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
@Service
public class MongoDBManager {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    @Qualifier("syncTestMongoDatabase")
    private MongoDatabase syncTestMongoDatabase;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    @Qualifier("reactivestreamsTestMongoDatabase")
    private com.mongodb.reactivestreams.client.MongoDatabase reactivestreamsTestMongoDatabase;

    @PostConstruct
    public void init() {
        log.debug("Sync Test: {}", syncTestMongoDatabase);
        var names = StreamSupport.stream(syncTestMongoDatabase.listCollectionNames().spliterator(), false)
                .collect(Collectors.toList());
        log.debug("Collection names: {}", names);
        log.debug("Reactive Streams Test: {}", reactivestreamsTestMongoDatabase);
        names = Flux.from(reactivestreamsTestMongoDatabase.listCollectionNames()).collectList().block();
        log.debug("Collection names: {}", names);
    }

}
