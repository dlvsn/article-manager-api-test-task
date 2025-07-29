package denys.mazurenko.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import denys.mazurenko.dto.author.MostActiveAuthorsResponseDto;
import denys.mazurenko.util.StaticObjectFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import java.util.List;

import static denys.mazurenko.util.SqlScripts.DELETE_ARTICLES_SQL;
import static denys.mazurenko.util.SqlScripts.DELETE_ROLES_SQL;
import static denys.mazurenko.util.SqlScripts.DELETE_USERS_SQL;
import static denys.mazurenko.util.SqlScripts.INSERT_ARTICLES_SQL;
import static denys.mazurenko.util.SqlScripts.INSERT_ROLES_FOR_USERS_SQL;
import static denys.mazurenko.util.SqlScripts.INSERT_USERS_SQL;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {
        INSERT_USERS_SQL,
        INSERT_ARTICLES_SQL,
        INSERT_ROLES_FOR_USERS_SQL
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {
        DELETE_ROLES_SQL,
        DELETE_ARTICLES_SQL,
        DELETE_USERS_SQL
}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class AdminControllerTest {
    private static final String TOP_ACTIVE_ENDPOINT = "/admin/statistics/authors/top-active";
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
    @WithMockUser(username = "admin", roles = "ADMIN")
    void findMostActiveAuthors_asAdmin_success() throws Exception {
        MvcResult jsonRequest = mockMvc.perform(
                MockMvcRequestBuilders.get(TOP_ACTIVE_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String jsonResponse = jsonRequest.getResponse().getContentAsString();
        List<MostActiveAuthorsResponseDto> expected = StaticObjectFactory.initExpectedList();
        List<MostActiveAuthorsResponseDto> actual = objectMapper.readValue(jsonResponse, new TypeReference<>() {});

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @WithUserDetails("testEmail2")
    void findMostActiveAuthors_asUser_forbidden() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.get(TOP_ACTIVE_ENDPOINT)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn();
    }
}