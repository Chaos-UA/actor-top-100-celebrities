package com.demo.actors.imdb;

import com.demo.actors.exception.ConflictException;
import com.demo.actors.exception.InternalException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@Slf4j
public class ImdbService {

    private final ImdbWebPageParser webPageParser;
    private final ThreadPoolTaskExecutor executor;

    private final AtomicBoolean syncInProgress = new AtomicBoolean(false);

    public ImdbService(ImdbConfig imdbConfig, ImdbWebPageParser webPageParser) {
        this.webPageParser = webPageParser;
        this.executor = new ThreadPoolTaskExecutor();
        executor.setBeanName("imdb-task-executor");
        executor.setThreadNamePrefix("imdb-thread-");
        executor.setCorePoolSize(imdbConfig.getMaxImdbParallelRequestsCount());
        executor.setMaxPoolSize(imdbConfig.getMaxImdbParallelRequestsCount());
        executor.initialize();
    }

    /**
     * Operation is heavy and will throw conflict exception in case if is already called.
     * @return top 100 celebrity actors from IMDB.
     * @throws ConflictException conflict exception in case if operation is already in progress by parallel thread.
     * TODO instead of parsing web page perhaps possible it is possible to find API to get what we need.
     */
    public List<ImdbActor> getTop100CelebrityActors() {
        if (!syncInProgress.compareAndSet(false, true)) {
            throw new ConflictException("Already in progress");
        }
        log.info("Fetching top 100 celebrities");
        try {
            List<ImdbCelebrityActor> celebrityTop100List = webPageParser.getCelebrityTop100Actors();
            List<Future<ImdbActor>> futureActors = new ArrayList<>(celebrityTop100List.size());
            for (ImdbCelebrityActor celebrityActor : celebrityTop100List) {
                futureActors.add(executor.submit(() -> {
                    log.info("Fetching actor details by actor ID {} with position {}, t: " + Thread.currentThread().getName(),
                            celebrityActor.getId(), celebrityActor.getPosition());
                    return webPageParser.getActor(celebrityActor.getId());
                }));
            }

            List<ImdbActor> result = new ArrayList<>(celebrityTop100List.size());
            for (Future<ImdbActor> futureActor : futureActors) {
                try {
                    result.add(futureActor.get());
                } catch (Exception e) {
                    futureActors.forEach(v -> v.cancel(true));
                    throw new InternalException("Failed to fetch top 100 celebrity actors", e);
                }
            }
            log.info("Finished fetching top 100 celebrities");
            return result;
        } finally {
            syncInProgress.set(false);
        }
    }

}
