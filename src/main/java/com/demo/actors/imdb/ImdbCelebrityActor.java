package com.demo.actors.imdb;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ImdbCelebrityActor {

    private String id;
    private Integer position;
}
