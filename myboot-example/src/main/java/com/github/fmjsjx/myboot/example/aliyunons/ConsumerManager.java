package com.github.fmjsjx.myboot.example.aliyunons;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.Consumer;

import io.netty.util.CharsetUtil;

/**
 * Consumer Manager component.
 */
@Component
public class ConsumerManager {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    @Qualifier("testONSConsumer")
    private Consumer testONSConsumer;

    /**
     * Initialize method.
     */
    @PostConstruct
    public void init() {
        testONSConsumer.subscribe("TOPIC", "*", (message, context) -> {
            var body = new String(message.getBody(), CharsetUtil.UTF_8);
            System.err.println(message + " -- " + body);
            return Action.CommitMessage;
        });
        testONSConsumer.start();
    }

}
