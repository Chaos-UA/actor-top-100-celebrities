package com.demo.actors.celebrities;

import com.demo.actors.imdb.ImdbActor;
import org.springframework.stereotype.Component;

@Component
public class CelebrityActorMapper {

    public CelebrityActorDoc toCelebrityActorDoc(ImdbActor source) {
        return CelebrityActorDoc.builder()
                .id(source.getId())
                .name(source.getName())
                .jobs(source.getJobs())
                .gender(source.getGender())
                .dob(source.getBirthDate())
                .imageUri(source.getImageUri())
                .build();
    }

    public CelebrityActor toCelebrityActor(CelebrityActorDoc source) {
        return CelebrityActor.builder()
                .name(source.getName())
                .jobs(source.getJobs())
                .gender(source.getGender())
                .dob(source.getDob())
                .imageUri(source.getImageUri())
                .build();
    }

}
