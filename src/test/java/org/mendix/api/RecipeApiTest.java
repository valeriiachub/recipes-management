package org.mendix.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mendix.api.security.AuthenticationFilter;
import org.mendix.api.security.SecurityConfig;
import org.mendix.dto.RecipeMlRequest;
import org.mendix.dto.response.RecipeListWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@Tag("intg")
@Rollback
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class RecipeApiTest {

    @Autowired
    private MockMvc mockMvc;

    private static List<RecipeMlRequest> requests;

    @BeforeEach
    public void before() {
        requests = loadRecipes();
    }

    @Test
    public void testCreateRecipe() throws Exception {
        RecipeMlRequest request = requests.get(0);
        HttpHeaders httpHeaders = prepareApiKeyHeader();

        MvcResult mvcResult = mockMvc.perform(post("/recipes")
                                             .with(csrf())
                                             .contentType("application/xml")
                                             .content(serializeToXml(request))
                                             .headers(httpHeaders))
                                     .andExpect(status().isOk()).andReturn();

        assertNotNull(mvcResult.getResponse());
    }

    @Test
    public void testGetRecipe() throws Exception {
        RecipeMlRequest request = requests.get(0);
        HttpHeaders httpHeaders = prepareApiKeyHeader();

        mockMvc.perform(post("/recipes")
                       .with(csrf())
                       .contentType("application/xml")
                       .content(serializeToXml(request))
                       .headers(httpHeaders))
               .andExpect(status().isOk()).andReturn();

        MvcResult mvcResult = mockMvc.perform(get("/recipes/1")
                                             .with(csrf())
                                             .contentType("application/xml")
                                             .headers(httpHeaders))
                                     .andExpect(status().isOk()).andReturn();

        assertNotNull(mvcResult.getResponse());
    }

    @Test
    public void testListRecipes() throws Exception {
        RecipeMlRequest request = requests.get(0);
        HttpHeaders httpHeaders = prepareApiKeyHeader();
        mockMvc.perform(post("/recipes")
                       .with(csrf())
                       .contentType("application/xml")
                       .content(serializeToXml(request))
                       .headers(httpHeaders))
               .andExpect(status().isOk()).andReturn();

        MvcResult mvcResult = mockMvc.perform(get("/recipes")
                                             .with(csrf())
                                             .contentType("application/xml")
                                             .headers(httpHeaders))
                                     .andExpect(status().isOk()).andReturn();

        assertNotNull(mvcResult.getResponse());
        String responseBody = mvcResult.getResponse().getContentAsString();
        RecipeListWrapper recipes = deserializeToObject(responseBody);
        assertFalse(recipes.getRecipes().isEmpty());
    }

    @Test
    public void testListRecipesByCategory() throws Exception {
        RecipeMlRequest request = requests.get(0);
        HttpHeaders httpHeaders = prepareApiKeyHeader();
        mockMvc.perform(post("/recipes")
                       .with(csrf())
                       .contentType("application/xml")
                       .content(serializeToXml(request))
                       .headers(httpHeaders))
               .andExpect(status().isOk()).andReturn();

        String category = "Chili";
        MvcResult mvcResult = mockMvc.perform(get("/recipes?category=" + category)
                                             .with(csrf())
                                             .contentType("application/xml")
                                             .headers(httpHeaders))
                                     .andExpect(status().isOk()).andReturn();

        assertNotNull(mvcResult.getResponse());
        String responseBody = mvcResult.getResponse().getContentAsString();
        RecipeListWrapper recipes = deserializeToObject(responseBody);
        assertFalse(recipes.getRecipes().isEmpty());
    }

    @Test
    public void testListRecipesByNonExistentCategoryCategory() throws Exception {
        RecipeMlRequest request = requests.get(0);
        HttpHeaders httpHeaders = prepareApiKeyHeader();
        mockMvc.perform(post("/recipes")
                       .with(csrf())
                       .contentType("application/xml")
                       .content(serializeToXml(request))
                       .headers(httpHeaders))
               .andExpect(status().isOk()).andReturn();

        String category = "Chocolate";
        MvcResult mvcResult = mockMvc.perform(get("/recipes?category=" + category)
                                             .with(csrf())
                                             .contentType("application/xml")
                                             .headers(httpHeaders))
                                     .andExpect(status().isOk()).andReturn();

        assertNotNull(mvcResult.getResponse());
        String responseBody = mvcResult.getResponse().getContentAsString();
        RecipeListWrapper recipes = deserializeToObject(responseBody);
        assertNull(recipes.getRecipes());
    }

    private List<RecipeMlRequest> loadRecipes() {
        List<RecipeMlRequest> requests = new ArrayList<>();
        try (Stream<Path> pathStream = Files.list(Paths.get("src/test/resources/recipes"))) {
            List<File> xmlFiles = pathStream
                    .filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".xml"))
                    .map(Path::toFile)
                    .collect(Collectors.toList());

            ObjectMapper objectMapper = new XmlMapper();

            for (File file : xmlFiles) {
                String xmlContent = Files.readString(file.toPath());
                RecipeMlRequest request = objectMapper.readValue(xmlContent, RecipeMlRequest.class);
                requests.add(request);
            }
        } catch (IOException e) {
            log.error("Recipes loading failed. Reason: {}", e.getMessage(), e);
        }
        return requests;
    }

    private HttpHeaders prepareApiKeyHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-api-key", "ValeriiaChub");

        return headers;
    }

    private String serializeToXml(RecipeMlRequest request) throws JsonProcessingException {
        XmlMapper xmlMapper = new XmlMapper();
        return xmlMapper.writeValueAsString(request);
    }

    private RecipeListWrapper deserializeToObject(String response) {
        XmlMapper xmlMapper = new XmlMapper();
        RecipeListWrapper wrapper = null;
        try {
            wrapper = xmlMapper.readValue(response, RecipeListWrapper.class);
        } catch (IOException e) {
            log.error("Unable to convert response. Reason {}", e.getMessage(), e);
        }

        return wrapper;
    }

}
