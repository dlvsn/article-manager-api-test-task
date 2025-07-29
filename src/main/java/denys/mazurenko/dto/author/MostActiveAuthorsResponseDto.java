package denys.mazurenko.dto.author;

public record MostActiveAuthorsResponseDto(Long authorId,
                                           Long countOfArticles) {
}
