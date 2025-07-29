package denys.mazurenko.service;

import static org.assertj.core.api.Assertions.assertThat;

import denys.mazurenko.dto.article.ArticleResponseDto;
import denys.mazurenko.dto.article.CreateArticleRequestDto;
import denys.mazurenko.entity.Article;
import denys.mazurenko.entity.User;
import denys.mazurenko.mapper.ArticleMapper;
import denys.mazurenko.repository.ArticleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@ExtendWith(MockitoExtension.class)
class ArticleServiceImplTest {
    private static final int DEFAULT_PAGE_SIZE = 20;
    private static final int FIRST = 0;
    @InjectMocks
    private ArticleServiceImpl articleService;

    @Mock
    private ArticleMapper articleMapper;

    @Mock
    private ArticleRepository articleRepository;

    private final List<Article> articles = new ArrayList<>();

    @Test
    void getAllArticles_returnsPageOfArticles() {
        initArticleList();
        Pageable pageable = PageRequest.of(0, DEFAULT_PAGE_SIZE);
        Page<Article> articlePage = new PageImpl<>(articles.subList(0, DEFAULT_PAGE_SIZE), pageable, articles.size());

        Article article = articles.get(FIRST);
        ArticleResponseDto expectedDto = initResponseDto(article);

        Mockito.when(articleRepository.findArticleFetchUsers(pageable)).thenReturn(articlePage);
        Mockito.when(articleMapper.toDto(article)).thenReturn(expectedDto);

        Page<ArticleResponseDto> result = articleService.getAllArticles(pageable);
        List<ArticleResponseDto> content = result.getContent();

        assertThat(result).isNotNull();
        assertThat(content).hasSize(DEFAULT_PAGE_SIZE);

        Mockito.verify(articleRepository, Mockito.times(1)).findArticleFetchUsers(pageable);
        for (int i = 0; i < DEFAULT_PAGE_SIZE; i++) {
            Mockito.verify(articleMapper).toDto(articles.get(i));
        }
    }

    @Test
    void addArticle_withNotNullDto_success() {
        CreateArticleRequestDto requestDto = new CreateArticleRequestDto("Test title", "Test content");
        Article article = new Article();
        article.setTitle(requestDto.title());
        article.setContent(requestDto.content());
        article.setCreatedAt(LocalDateTime.now());
        User testUser = initUser();
        article.setAuthor(testUser);

        Mockito.when(articleMapper.toEntity(requestDto)).thenReturn(article);

        ArticleResponseDto expectedDto = initResponseDto(article);

        Mockito.when(articleRepository.save(article)).thenReturn(article);
        Mockito.when(articleMapper.toDto(article)).thenReturn(expectedDto);

        ArticleResponseDto articleResponseDto = articleService.saveArticle(requestDto, testUser);

        assertThat(articleResponseDto).isNotNull();
        assertThat(articleResponseDto).isEqualTo(expectedDto);

        Mockito.verify(articleMapper, Mockito.times(1)).toEntity(requestDto);
        Mockito.verify(articleRepository, Mockito.times(1)).save(article);
        Mockito.verify(articleMapper, Mockito.times(1)).toDto(article);
    }

    private void initArticleList() {
        for (int i = 0; i < 30; i++) {
            User user = new User();
            user.setEmail("email" + i);
            user.setPassword("password");
            Article article = new Article();
            article.setTitle("title" + i);
            article.setContent("content" + i);
            article.setAuthor(user);
            article.setCreatedAt(LocalDateTime.now());
            articles.add(article);
        }
    }

    private User initUser() {
        User user = new User();
        user.setEmail("email");
        user.setPassword("password");
        return user;
    }

    private ArticleResponseDto initResponseDto(Article article) {
        return new ArticleResponseDto(
                article.getTitle(),
                article.getContent(),
                article.getAuthor().getId(),
                article.getCreatedAt());
    }
}