package com.github.fmjsjx.myboot.example.http.router;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.fmjsjx.libnetty.http.server.middleware.Router;

import lombok.extern.slf4j.Slf4j;

/**
 * Test router.
 */
@Slf4j
@Component
public class TestRouter {

    @Autowired
    private Router router;

    @PostConstruct
    private void init() {
        log.debug("Router: {}", router);
    }

}
