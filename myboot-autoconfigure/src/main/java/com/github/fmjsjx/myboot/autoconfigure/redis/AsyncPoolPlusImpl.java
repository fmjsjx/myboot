package com.github.fmjsjx.myboot.autoconfigure.redis;

import java.util.concurrent.CompletableFuture;

import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.support.AsyncPool;

class AsyncPoolPlusImpl<K, V, C extends StatefulRedisConnection<K, V>> implements AsyncPoolPlus<K, V, C> {

    private final AsyncPool<C> pool;

    public AsyncPoolPlusImpl(AsyncPool<C> pool) {
        this.pool = pool;
    }

    @Override
    public CompletableFuture<C> acquire() {
        return pool.acquire();
    }

    @Override
    public CompletableFuture<Void> release(C object) {
        return pool.release(object);
    }

    @Override
    public void clear() {
        pool.clear();
    }

    @Override
    public CompletableFuture<Void> clearAsync() {
        return pool.clearAsync();
    }

    @Override
    public void close() {
        pool.close();
    }

    @Override
    public CompletableFuture<Void> closeAsync() {
        return pool.closeAsync();
    }

}
