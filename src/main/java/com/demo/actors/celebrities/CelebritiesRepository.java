package com.demo.actors.celebrities;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CelebritiesRepository extends MongoRepository<CelebrityActor, String> {

    @Query
    List<CelebrityActor> findAllByJobs(String job);

}
