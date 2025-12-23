package com.github.fmjsjx.myboot.example.http.router;

import com.github.fmjsjx.libnetty.http.server.middleware.Router;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * Test router.
 */
@Slf4j
@Component
public class TestRouter implements InitializingBean {

    private final Router router;

    /**
     * Constructs a new {@link TestRouter} instance
     *
     * @param router the router
     */
    public TestRouter(Router router) {
        this.router = router;
    }

    @Override
    public void afterPropertiesSet() {
        log.debug("Router: {}", router);
    }

}
