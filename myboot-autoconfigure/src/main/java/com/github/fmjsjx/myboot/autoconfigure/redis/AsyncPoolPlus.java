package com.github.fmjsjx.myboot.autoconfigure.redis;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;
import java.util.function.Function;

import com.github.fmjsjx.libcommon.redis.RedisPoolUtil;

import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.support.AsyncPool;

/**
 * Extra interface for {@link AsyncPool}.
 *
 * @param <K> the key type
 * @param <V> the value type
 * @param <C> the connection type
 */
public interface AsyncPoolPlus<K, V, C extends StatefulRedisConnection<K, V>> extends AsyncPool<C> {

    /**
     * Acquire a connection from this {@code pool} and then apply the specified
     * {@code action}. The acquired connection will be released back to this pool
     * automatically.
     * 
     * @param <R>    the return type
     * @param action the action
     * @return a {@code CompletableFuture<R>}
     */
    default <R> CompletableFuture<R> apply(Function<C, CompletionStage<R>> action) {
        return RedisPoolUtil.apply(this, action);
    }

    /**
     * Acquire a connection from this {@code pool} and then apply the specified
     * {@code action} asynchronously. The acquired connection will be released back
     * to this pool automatically.
     * 
     * @param <R>    the return type
     * @param action the action
     * @return a {@code CompletableFuture<R>}
     */
    default <R> CompletableFuture<R> applyAsync(Function<C, CompletionStage<R>> action) {
        return RedisPoolUtil.applyAsync(this, action);
    }

    /**
     * Acquire a connection from this {@code pool} and then apply the specified
     * {@code action} asynchronously. The acquired connection will be released back
     * to this pool automatically.
     * 
     * @param <R>      the return type
     * @param action   the action
     * @param executor the executor to use for asynchronous execution
     * @return a {@code CompletableFuture<R>}
     */
    default <R> CompletableFuture<R> applyAsync(Function<C, CompletionStage<R>> action, Executor executor) {
        return RedisPoolUtil.applyAsync(this, action, executor);
    }

    /**
     * Acquire a connection from this {@code pool} and then execute the specified
     * {@code action}. The acquired connection will be released back to the pool
     * automatically.
     * 
     * @param action the action
     * @return a {@code CompletableFuture<Void>}
     */
    default CompletableFuture<Void> accept(Function<C, CompletionStage<Void>> action) {
        return RedisPoolUtil.accept(this, action);
    }

    /**
     * Acquire a connection from this {@code pool} and then execute the specified
     * {@code action} asynchronously. The acquired connection will be released back
     * to the pool automatically.
     * 
     * @param action the action
     * @return a {@code CompletableFuture<Void>}
     */
    default CompletableFuture<Void> acceptAsync(Function<C, CompletionStage<Void>> action) {
        return RedisPoolUtil.acceptAsync(this, action);
    }

    /**
     * Acquire a connection from this {@code pool} and then execute the specified
     * {@code action} asynchronously. The acquired connection will be released back
     * to the pool automatically.
     * 
     * @param action   the action
     * @param executor the executor to use for asynchronous execution
     * @return a {@code CompletableFuture<Void>}
     */
    default CompletableFuture<Void> acceptAsync(Function<C, CompletionStage<Void>> action, Executor executor) {
        return RedisPoolUtil.acceptAsync(this, action, executor);
    }

}
