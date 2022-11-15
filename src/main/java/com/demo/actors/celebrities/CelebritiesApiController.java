package com.demo.actors.celebrities;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CelebritiesApiController {

    private final CelebritiesService celebritiesService;

    public CelebritiesApiController(CelebritiesService celebritiesService) {
        this.celebritiesService = celebritiesService;
    }

    @GetMapping("/api/v1/actors/top-100-celebrities")
    public List<CelebrityActor> getCelebrities(@RequestParam("job") String job) {
        return celebritiesService.getCelebrities(job);
    }

    @PostMapping("/api/v1/actors/reset-top-100-celebrities")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void resetCelebrities() {
        celebritiesService.resetCelebrities();
    }

}