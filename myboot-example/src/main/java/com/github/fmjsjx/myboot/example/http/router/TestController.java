package com.github.fmjsjx.myboot.example.http.router;

import java.util.concurrent.CompletionStage;

import com.github.fmjsjx.libnetty.http.server.HttpRequestContext;
import com.github.fmjsjx.libnetty.http.server.HttpResult;
import com.github.fmjsjx.libnetty.http.server.annotation.HttpGet;
import com.github.fmjsjx.myboot.http.route.annotation.RouteController;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.HttpResponseStatus;

@RouteController
public class TestController {

    private static final ByteBuf OK = Unpooled
            .unreleasableBuffer(ByteBufAllocator.DEFAULT.buffer(2, 2).writeBytes("ok".getBytes()).asReadOnly());

    @HttpGet("/api/test")
    public CompletionStage<HttpResult> test(HttpRequestContext ctx) {
        return ctx.simpleRespond(HttpResponseStatus.OK, OK.duplicate(), 2, HttpRequestContext.TEXT_PLAIN_UTF8);
    }

}
