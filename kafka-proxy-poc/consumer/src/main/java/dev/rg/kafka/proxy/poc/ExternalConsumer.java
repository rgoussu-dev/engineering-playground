package dev.rg.kafka.proxy.poc;

import io.opentelemetry.instrumentation.annotations.WithSpan;
import io.quarkus.runtime.StartupEvent;
import org.eclipse.microprofile.reactive.messaging.*;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

import java.util.logging.Logger;
import java.util.stream.Stream;

@ApplicationScoped
public class ExternalConsumer {

    private static final Logger LOGGER = Logger.getLogger(ExternalConsumer.class.getName());

    @WithSpan
    @Incoming("incoming-channel")
    public void sink(String message) {
        LOGGER.info("Received tac for my tic: %s".formatted(message));
    }
}
