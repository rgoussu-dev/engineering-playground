package dev.rg;

import io.kroxylicious.proxy.filter.FetchResponseFilter;
import io.kroxylicious.proxy.filter.FilterContext;
import io.kroxylicious.proxy.filter.ResponseFilterResult;
import org.apache.kafka.common.message.FetchResponseData;
import org.apache.kafka.common.message.ResponseHeaderData;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

public class MockKroxiliciousConsumeFilter implements FetchResponseFilter {

    @Override
    public CompletionStage<ResponseFilterResult> onFetchResponse(short apiVersion, ResponseHeaderData header, FetchResponseData response, FilterContext context) {
        return CompletableFuture.supplyAsync(() -> response, CompletableFuture.delayedExecutor(800, TimeUnit.MILLISECONDS))
                .thenCompose(resp -> context.forwardResponse(
                        header,
                        response)
                );
    }
}
