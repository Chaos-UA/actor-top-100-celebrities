package com.demo.actors.imdb;

import com.demo.actors.common.model.Gender;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ImdbWebPageParserIT {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ImdbWebPageParser unit = new ImdbWebPageParser(URI.create("https://www.imdb.com"), objectMapper);

    @Test
    void should_fetch_top_100_actor_celebrities() {
        // when
        List<ImdbCelebrityActor> topCelebrityList = unit.getCelebrityTop100Actors();

        // then
        assertThat(topCelebrityList.size()).isEqualTo(100);
        topCelebrityList.forEach(item -> {
            assertThat(item.getPosition())
                    .isNotEqualTo(0)
                    .isNotNull();
            assertThat(item.getId()).isNotNull();
        });
    }

    @Test
    void should_fetch_chuck_norris() {
        // given
        String actorId = "nm0001569";

        // when
        ImdbActor actor = unit.getActor(actorId);

        // then
        assertThat(actor.getId()).isEqualTo(actorId);
        assertThat(actor.getName()).isEqualTo("Chuck Norris");
        assertThat(actor.getBirthDate()).isEqualTo(LocalDate.of(1940, 3, 10));
        assertThat(actor.getGender()).isEqualTo(Gender.MALE);
        assertThat(actor.getJobs()).contains("Actor", "Music Department", "Producer");
        assertThat(actor.getImageUri()).isNotNull();
    }

}
