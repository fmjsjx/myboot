package com.github.fmjsjx.myboot.autoconfigure.mongodb;

import java.util.Optional;
import java.util.function.Supplier;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

import com.github.fmjsjx.myboot.autoconfigure.mongodb.MongoDBProperties.MongoClientProperties;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class SyncMongoClientRegistry {

    static final void register(BeanDefinitionRegistry registry, MongoClientProperties config) {
        var name = config.getName();
        var beanName = Optional.ofNullable(config.getBeanName()).orElseGet(() -> name + "MongoClient");
        var beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(MongoClient.class, beanFactory(config))
                .setScope(BeanDefinition.SCOPE_SINGLETON).setPrimary(config.isPrimary()).getBeanDefinition();
        log.debug("Register sync mongo client bean definition '{}' >>> {}", beanName, beanDefinition);
        registry.registerBeanDefinition(beanName, beanDefinition);
        Optional.ofNullable(config.getDatabases()).ifPresent(dbs -> {
            if (config.isPrimary()) {
                if (dbs.size() == 1) {
                    dbs.get(0).setPrimary(true);
                }
            } else {
                dbs.forEach(db -> db.setPrimary(false));
            }
            dbs.forEach(db -> {
                var dbname = db.getName();
                var bean = Optional.ofNullable(db.getBeanName()).orElseGet(() -> db.getId() + "MongoDatabase");
                var definition = BeanDefinitionBuilder.genericBeanDefinition(MongoDatabase.class)
                        .setFactoryMethodOnBean("getDatabase", beanName).addConstructorArgValue(dbname)
                        .setScope(BeanDefinition.SCOPE_SINGLETON).setPrimary(db.isPrimary()).addDependsOn(beanName)
                        .getBeanDefinition();
                log.debug("Register sync mongo database bean definition '{}' >>> {}", beanName, definition);
                registry.registerBeanDefinition(bean, definition);
            });
        });
    }

    private static final Supplier<MongoClient> beanFactory(MongoClientProperties config) {
        return () -> MongoClients.create(MongoClientSettingsFactory.create(config));
    }

}
