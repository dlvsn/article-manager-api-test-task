package denys.mazurenko.repository;

import static denys.mazurenko.util.StaticObjectFactory.initExpectedList;
import static denys.mazurenko.util.SqlScripts.DELETE_ARTICLES_SQL;
import static denys.mazurenko.util.SqlScripts.DELETE_ROLES_SQL;
import static denys.mazurenko.util.SqlScripts.DELETE_USERS_SQL;
import static denys.mazurenko.util.SqlScripts.INSERT_ARTICLES_SQL;
import static denys.mazurenko.util.SqlScripts.INSERT_USERS_SQL;
import static org.assertj.core.api.Assertions.assertThat;

import denys.mazurenko.dto.author.MostActiveAuthorsResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;
import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = {
        INSERT_USERS_SQL,
        INSERT_ARTICLES_SQL,
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {
        DELETE_ROLES_SQL,
        DELETE_ARTICLES_SQL,
        DELETE_USERS_SQL
}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ArticleRepositoryTest {
    private static final int EXPECTED_LIST_SIZE = 3;
    @Autowired
    private ArticleRepository articleRepository;

    @Test
    void findMostActiveAuthors_returnsListWithCountOfArticlesAndAuthorsIds() {
        List<MostActiveAuthorsResponseDto> expected = initExpectedList();
        List<MostActiveAuthorsResponseDto> actual = articleRepository.findMostActiveAuthors(
                LocalDateTime.now().minusDays(50),
                PageRequest.of(0, EXPECTED_LIST_SIZE)
        );
        assertThat(actual).hasSize(EXPECTED_LIST_SIZE);
        assertThat(actual).isEqualTo(expected);
    }
}