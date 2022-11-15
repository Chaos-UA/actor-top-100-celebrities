package com.demo.actors.imdb;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class ImdbConfig {

    private final int maxImdbParallelRequestsCount;

    public ImdbConfig(@Value("${imdb.maxParallelRequestsCount}") int maxImdbParallelRequestsCount) {
        this.maxImdbParallelRequestsCount = maxImdbParallelRequestsCount;
    }

}
