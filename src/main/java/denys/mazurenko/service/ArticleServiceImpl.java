package denys.mazurenko.service;

import denys.mazurenko.dto.article.ArticleResponseDto;
import denys.mazurenko.dto.article.CreateArticleRequestDto;
import denys.mazurenko.entity.Article;
import denys.mazurenko.entity.User;
import denys.mazurenko.mapper.ArticleMapper;
import denys.mazurenko.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {
    private final ArticleMapper articleMapper;
    private final ArticleRepository articleRepository;

    @Override
    public Page<ArticleResponseDto> getAllArticles(Pageable pageable) {
        return articleRepository.findArticleFetchUsers(pageable)
                .map(articleMapper::toDto);
    }

    @Override
    public ArticleResponseDto saveArticle(CreateArticleRequestDto requestDto, User user) {
        Article article = articleMapper.toEntity(requestDto);
        article.setAuthor(user);
        return articleMapper.toDto(articleRepository.save(article));
    }
}
