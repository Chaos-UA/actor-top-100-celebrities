package com.demo.actors.imdb;

import com.demo.actors.common.model.Gender;
import com.demo.actors.exception.InternalException;
import com.demo.actors.exception.ParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ImdbWebPageParser {

    private static final Pattern PARSE_PAGE_JSON_PATTERN = Pattern.compile("<script type=\"application/ld\\+json\">(.+?)</script>", Pattern.DOTALL);
    private static final Pattern ACTOR_ID_URL_PATTERN = Pattern.compile("/name/(\\w+)/", Pattern.DOTALL);
    private static final Set<String> MALE_JOB_TITLES = Set.of("Actor");
    private static final Set<String> FEMALE_JOB_TILES = Set.of("Actress");

    private final URI baseUrl;
    private final ObjectMapper objectMapper;
    private final HttpClient client;

    public ImdbWebPageParser(@Value("${imdb.baseUrl}") URI baseUrl, ObjectMapper objectMapper) {
        this.baseUrl = baseUrl;
        this.objectMapper = objectMapper;
        this.client = HttpClient.newBuilder().build();
    }

    /**
     * @return 100 most popular celebrities in the world.
     */
    public List<ImdbCelebrityActor> getCelebrityTop100Actors() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(String.format("%s/list/ls052283250/", baseUrl)))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JsonNode top100RootJson = parsePageJson(response.body());

            List<ImdbCelebrityActor> result = new ArrayList<>(100);
            top100RootJson.path("about").path("itemListElement").forEach(itemJson -> {
                String url = itemJson.path("url").textValue();
                Matcher idMatcher = ACTOR_ID_URL_PATTERN.matcher(url);
                if (!idMatcher.find()) {
                    throw new ParseException("Failed to parse ID from the URL: " + url);
                }
                result.add(ImdbCelebrityActor.builder()
                        .id(idMatcher.group(1))
                        .position(itemJson.get("position").asInt())
                        .build());
            });

            if (result.size() != 100) {
                throw new IllegalStateException(String.format("Failed to parse top 100 actor celebrities. " +
                        "Parsed actors count: %s", result.size()));
            }

            return result;
        } catch (Exception e) {
            throw new InternalException("Failed to get top 100 actor celebrities", e);
        }
    }

    public ImdbActor getActor(String actorId) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(String.format("%s/name/%s/", baseUrl, actorId)))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JsonNode rootJson = parsePageJson(response.body());
            Set<String> jobs = new HashSet<>();
            rootJson.get("jobTitle").forEach(jobNode -> jobs.add(jobNode.textValue()));

            return ImdbActor.builder()
                    .id(actorId)
                    .name(rootJson.get("name").textValue())
                    .imageUri(rootJson.get("image").textValue())
                    .description(rootJson.get("description").textValue())
                    .jobs(jobs)
                    .gender(determineGender(jobs).orElse(null))
                    .birthDate(LocalDate.parse(rootJson.get("birthDate").textValue()))
                    .build();
        } catch (Exception e) {
            throw new InternalException("Failed to get actor details", e);
        }
    }

    private JsonNode parsePageJson(String pageContent) {
        Matcher matcher = PARSE_PAGE_JSON_PATTERN.matcher(pageContent);

        if (!matcher.find()) {
            throw new ParseException("Failed to parse page json content");
        }

        try {
            return objectMapper.readTree(matcher.group(1));
        } catch (Exception e) {
            throw new ParseException("Failed to parse page json content", e);
        }
    }

    /**
     * Currently we are only able to detect gender by job title.
     * Would be good in future to extend the logic.
     */
    private Optional<Gender> determineGender(Set<String> jobs) {
        for (String jobTitle : FEMALE_JOB_TILES) {
            if (jobs.contains(jobTitle)) {
                return Optional.of(Gender.FEMALE);
            }
        }
        return MALE_JOB_TITLES.stream().anyMatch(jobs::contains)
                ? Optional.of(Gender.MALE)
                : Optional.empty();
    }

}
