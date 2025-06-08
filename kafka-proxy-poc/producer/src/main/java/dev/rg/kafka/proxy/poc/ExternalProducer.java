package dev.rg.kafka.proxy.poc;

import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;
import java.util.stream.IntStream;

@ApplicationScoped
public class ExternalProducer {
    private static final Logger LOGGER = Logger.getLogger(ExternalProducer.class.getName());
    private static final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
    @Inject
    @Channel("incoming-channel")
    Emitter<String> emitter;

    @Scheduled(every = "1s")
    void produces() {
        LOGGER.info("Producing 100 messages to channel");
        IntStream.range(0, 99).forEach(i -> {
            executor.execute(
                    () -> {
                        LOGGER.info("Single message %d".formatted(i));
                        emitter.send("This is the tic to your tac %s".formatted(System.currentTimeMillis()));
                        LOGGER.info("Message sent to channel");
                    }
            );
        });
    }
}
