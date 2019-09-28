package ru.fines.tools.fineschecker.services;

import com.google.common.collect.ImmutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.fines.tools.fineschecker.core.ServiceLivenessChecker;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static java.lang.Math.min;

public class LivenessCheckRunner {

    private static final Logger log = LoggerFactory.getLogger(LivenessCheckRunner.class);

    private final List<ServiceLivenessChecker> checkers;
    private final TelegramNotifier notifier;
    private final ExecutorService executor;
    private final boolean shouldSendSuccessMessage;
    private final String availabilityMessageSuffix;
    private final String unavailabilityMessageSuffix;

    public LivenessCheckRunner(Collection<ServiceLivenessChecker> checkers,
                               TelegramNotifier notifier,
                               boolean shouldSendSuccessMessage,
                               String availabilityMessageSuffix,
                               String unavailabilityMessageSuffix) {
        this.checkers = ImmutableList.copyOf(checkers);
        this.notifier = notifier;
        this.executor = Executors.newFixedThreadPool(min(checkers.size(), Runtime.getRuntime().availableProcessors()));
        this.shouldSendSuccessMessage = shouldSendSuccessMessage;
        this.availabilityMessageSuffix = availabilityMessageSuffix;
        this.unavailabilityMessageSuffix = unavailabilityMessageSuffix;
    }

    public void run() {
        List<CompletableFuture<?>> results = checkers.stream()
            .map(checker -> CompletableFuture.runAsync(() -> checkLiveness(checker), executor))
            .collect(Collectors.toList());
        CompletableFuture.allOf(toArray(results)).join();
    }

    private void checkLiveness(ServiceLivenessChecker checker) {
        log.info("Staring to check: serviceName={}", checker.getServiceName());
        boolean isAlive = checker.isServiceAlive();
        log.info("Service name status: serviceName={}, isAlive={}", checker.getServiceName(), isAlive);
        if (isAlive && shouldSendSuccessMessage) {
            notifier.sendMessage(checker.getServiceName() + " " + availabilityMessageSuffix);
        } else if (!isAlive) {
            notifier.sendMessage(checker.getServiceName() + " " + unavailabilityMessageSuffix);
        }
    }

    private static CompletableFuture<?>[] toArray(Collection<CompletableFuture<?>> futures) {
        return futures.toArray(new CompletableFuture[0]);
    }
}
