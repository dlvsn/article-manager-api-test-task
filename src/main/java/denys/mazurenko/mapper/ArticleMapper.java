package denys.mazurenko.mapper;

import denys.mazurenko.config.MapperConfig;
import denys.mazurenko.dto.article.ArticleResponseDto;
import denys.mazurenko.dto.article.CreateArticleRequestDto;
import denys.mazurenko.entity.Article;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface ArticleMapper {
    @Mapping(target = "authorId", source = "author.id")
    ArticleResponseDto toDto(Article article);

    Article toEntity(CreateArticleRequestDto createArticleRequestDto);
}
