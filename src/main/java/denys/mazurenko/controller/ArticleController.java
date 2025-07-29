package denys.mazurenko.controller;

import denys.mazurenko.dto.article.ArticleResponseDto;
import denys.mazurenko.dto.article.CreateArticleRequestDto;
import denys.mazurenko.entity.User;
import denys.mazurenko.security.CustomUserDetailsService;
import denys.mazurenko.service.ArticleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/articles")
public class ArticleController {
    private final CustomUserDetailsService userDetailsService;
    private final ArticleService articleService;

    @GetMapping
    public Page<ArticleResponseDto> getAllArticles(Pageable pageable) {
        return articleService.getAllArticles(pageable);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_PUBLISHER')")
    @ResponseStatus(HttpStatus.CREATED)
    public ArticleResponseDto addArticle(@RequestBody
                                         @Valid CreateArticleRequestDto requestDto,
                                         Authentication authentication) {
        User user = userDetailsService.loadUserByAuthentication(authentication);
        return articleService.saveArticle(requestDto, user);
    }
}
