package dev.rg;

import io.kroxylicious.proxy.filter.ProduceRequestFilter;
import io.kroxylicious.proxy.filter.RequestFilterResult;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

public class MockKroxiliciousProduceFilter implements ProduceRequestFilter {

    @Override
    public CompletionStage<RequestFilterResult> onProduceRequest(short apiVersion, org.apache.kafka.common.message.RequestHeaderData header, org.apache.kafka.common.message.ProduceRequestData request, io.kroxylicious.proxy.filter.FilterContext context) {
        // Simulate processing delay
        return CompletableFuture.supplyAsync(() -> request, CompletableFuture.delayedExecutor(800, TimeUnit.MILLISECONDS))
                .thenCompose(req -> context.forwardRequest(header, request));
    }
}
