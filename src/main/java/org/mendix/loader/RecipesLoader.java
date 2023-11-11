package org.mendix.loader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mendix.dto.RecipeMlRequest;
import org.mendix.service.RecipesManagementService;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component
@RequiredArgsConstructor
public class RecipesLoader {

    private final RecipesManagementService recipesManagementService;

    public void loadRecipes() {
        try (Stream<Path> pathStream = Files.list(Paths.get("src/main/resources/recipes"))) {
            List<File> xmlFiles = pathStream
                    .filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".xml"))
                    .map(Path::toFile)
                    .collect(Collectors.toList());

            if (!CollectionUtils.isEmpty(xmlFiles)) {
                List<RecipeMlRequest> requests = new ArrayList<>();
                ObjectMapper objectMapper = new XmlMapper();
                for (File file : xmlFiles) {
                    String xmlContent = Files.readString(file.toPath());
                    RecipeMlRequest request = objectMapper.readValue(xmlContent, RecipeMlRequest.class);
                    requests.add(request);
                }
                recipesManagementService.createRecipes(requests);
            }
        } catch (IOException e) {
            log.error("Recipes loading failed. Reason: {}", e.getMessage(), e);
        }
    }

}
