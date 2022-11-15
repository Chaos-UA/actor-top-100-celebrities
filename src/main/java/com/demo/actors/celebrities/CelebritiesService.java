package com.demo.actors.celebrities;

import com.demo.actors.imdb.ImdbActor;
import com.demo.actors.imdb.ImdbService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class CelebritiesService {

    private final CelebritiesRepository celebritiesRepo;
    private final ImdbService imdbService;
    private final CelebrityActorMapper celebrityActorMapper;

    public CelebritiesService(CelebritiesRepository celebritiesRepo,
                              ImdbService imdbService,
                              CelebrityActorMapper celebrityActorMapper) {
        this.celebritiesRepo = celebritiesRepo;
        this.imdbService = imdbService;
        this.celebrityActorMapper = celebrityActorMapper;
    }

    /**
     * @param job job title to filter jobs by.
     * @return filtered list of actors in top 100 celebrities.
     */
    public List<CelebrityActor> getCelebrities(String job) {
        return celebritiesRepo.findAllByJobs(job);
    }

    /**
     * Initiate synchronization of top 100 celebrities with remote IMDB service.
     */
    @Transactional
    public void resetCelebrities() {
        log.info("Resetting celebrities");
        List<ImdbActor> imdbActors = imdbService.getTop100CelebrityActors();
        List<CelebrityActor> celebrityActors = imdbActors.stream()
                .map(celebrityActorMapper::toCelebrityActor)
                .toList();
        celebritiesRepo.deleteAll();
        celebritiesRepo.saveAll(celebrityActors);
        log.info("Celebrities resetting has finished");
    }

}
