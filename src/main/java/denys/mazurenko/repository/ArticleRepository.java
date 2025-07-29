package denys.mazurenko.repository;

import denys.mazurenko.dto.author.MostActiveAuthorsResponseDto;
import denys.mazurenko.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    @Query("select art from Article art join fetch art.author")
    Page<Article> findArticleFetchUsers(Pageable pageable);

    @Query("select a.author.id as authorId, COUNT(a) from Article a "
            + "where a.createdAt >=:days group by a.author.id order by COUNT (a) desc")
    List<MostActiveAuthorsResponseDto> findMostActiveAuthors(@Param("days") LocalDateTime days,
                                                             Pageable pageable);
}
