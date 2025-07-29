package denys.mazurenko.controller;

import static denys.mazurenko.util.SqlScripts.DELETE_ARTICLES_SQL;
import static denys.mazurenko.util.SqlScripts.DELETE_ROLES_SQL;
import static denys.mazurenko.util.SqlScripts.DELETE_USERS_SQL;
import static denys.mazurenko.util.SqlScripts.INSERT_30_ARTICLES_SQL;
import static denys.mazurenko.util.SqlScripts.INSERT_ROLES_FOR_USERS_SQL;
import static denys.mazurenko.util.SqlScripts.INSERT_USERS_SQL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import denys.mazurenko.dto.article.ArticleResponseDto;
import denys.mazurenko.dto.article.CreateArticleRequestDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import java.time.LocalDateTime;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {
        INSERT_USERS_SQL,
        INSERT_30_ARTICLES_SQL,
        INSERT_ROLES_FOR_USERS_SQL
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {
        DELETE_ROLES_SQL,
        DELETE_ARTICLES_SQL,
        DELETE_USERS_SQL
}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ArticleControllerTest {
    private static final int EXPECTED_TOTAL_ELEMENTS = 30;
    private static final int EXPECTED_PAGE_SIZE = 20;
    private static final String ARTICLES_ENDPOINT = "/articles";
    private static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithUserDetails("testEmail2")
    void getAllArticles_asUser_returnsPageOfArticles() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.get(ARTICLES_ENDPOINT)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.length()").value(EXPECTED_PAGE_SIZE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(EXPECTED_TOTAL_ELEMENTS));
    }

    @Test
    @WithUserDetails("testEmail3")
    void addNewArticle_asPublisher_isCreated() throws Exception {
        CreateArticleRequestDto requestDto = new CreateArticleRequestDto(
                "testTitle",
                "testContent"
        );
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders
                        .post(ARTICLES_ENDPOINT)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andReturn();
        String jsonResponse = mvcResult.getResponse().getContentAsString();

        ArticleResponseDto actualResult = objectMapper.readValue(jsonResponse, ArticleResponseDto.class);
        ArticleResponseDto expectedResult = new ArticleResponseDto(
                requestDto.title(),
                requestDto.content(),
                3L,
                LocalDateTime.now());
        assertThat(actualResult)
                .usingRecursiveComparison()
                .ignoringFieldsOfTypes(LocalDateTime.class)
                .isEqualTo(expectedResult);
    }
}