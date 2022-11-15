package com.demo.actors.imdb;

import com.demo.actors.common.model.Gender;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
public class ImdbActor {

    private String id;
    private String name;

    @Indexed
    private Set<String> jobs;
    private Gender gender;
    private String imageUri;
    private LocalDate birthDate;
    private String description;

}
