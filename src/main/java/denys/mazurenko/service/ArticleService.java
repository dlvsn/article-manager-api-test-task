package denys.mazurenko.service;

import denys.mazurenko.dto.article.ArticleResponseDto;
import denys.mazurenko.dto.article.CreateArticleRequestDto;
import denys.mazurenko.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArticleService {
    Page<ArticleResponseDto> getAllArticles(Pageable pageable);

    ArticleResponseDto saveArticle(CreateArticleRequestDto requestDto, User user);
}
