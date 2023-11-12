package org.mendix;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mendix.loader.RecipesLoader;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@Slf4j
@RequiredArgsConstructor
@SpringBootApplication
public class RecipesManagement {

    private final RecipesLoader recipesLoader;

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(RecipesManagement.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }

    @PostConstruct
    public void loadRecipesOnStartup() {
        recipesLoader.loadRecipes();
    }
}