package com.demo.actors.celebrities;

import com.demo.actors.imdb.ImdbActor;
import org.springframework.stereotype.Component;

@Component
public class CelebrityActorMapper {

    public CelebrityActor toCelebrityActor(ImdbActor source) {
        return CelebrityActor.builder()
                .id(source.getId())
                .name(source.getName())
                .jobs(source.getJobs())
                .gender(source.getGender())
                .dob(source.getBirthDate())
                .imageUri(source.getImageUri())
                .build();
    }

}
