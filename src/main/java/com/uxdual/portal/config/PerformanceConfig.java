package com.uxdual.portal.config;

import io.micronaut.context.annotation.Factory;
import io.micronaut.core.annotation.Introspected;
import jakarta.inject.Singleton;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Factory
@Introspected
public class PerformanceConfig {

    @Singleton
    public ExecutorService asyncExecutor() {
        return Executors.newFixedThreadPool(10, r -> {
            Thread t = new Thread(r);
            t.setName("ux-dual-async-" + t.getId());
            t.setDaemon(true);
            return t;
        });
    }
}