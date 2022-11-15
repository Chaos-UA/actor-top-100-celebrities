package com.demo.actors.imdb;

import com.demo.actors.common.model.Gender;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
public class ImdbActor {

    private String id;
    private String name;
    private Set<String> jobs;
    private Gender gender;
    private String imageUri;
    private LocalDate birthDate;
    private String description;

}
