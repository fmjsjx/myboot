package com.github.fmjsjx.myboot.example.aliyunons;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Consumer;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;

import io.netty.util.CharsetUtil;

@Component
public class ConsumerManager {

    @Autowired
    @Qualifier("testONSConsumer")
    private Consumer testONSConsumer;

    @PostConstruct
    public void init() {
        testONSConsumer.subscribe("TOPIC", "*", new MessageListener() {
            @Override
            public Action consume(Message message, ConsumeContext context) {
                var body = new String(message.getBody(), CharsetUtil.UTF_8);
                System.err.println(message + " -- " + body);
                return Action.CommitMessage;
            }
        });
        testONSConsumer.start();
    }

}
