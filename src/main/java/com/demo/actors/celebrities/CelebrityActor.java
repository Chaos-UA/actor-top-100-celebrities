package com.demo.actors.celebrities;

import com.demo.actors.common.model.Gender;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
public class CelebrityActor {

    private String name;
    private LocalDate dob;
    private Gender gender;
    private Set<String> jobs;
    private String imageUri;

}
