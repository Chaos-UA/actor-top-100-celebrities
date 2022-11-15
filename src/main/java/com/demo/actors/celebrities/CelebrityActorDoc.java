package com.demo.actors.celebrities;

import com.demo.actors.common.model.Gender;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.Set;

@Document("Top100CelebrityActor")
@Data
@Builder
public class CelebrityActorDoc {

    @Id
    private String id;
    private String name;
    private LocalDate dob;
    private Gender gender;

    @Indexed
    private Set<String> jobs;
    private String imageUri;

}