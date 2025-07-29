package denys.mazurenko.dto.article;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public record ArticleResponseDto(String title,
                                 String content,
                                 Long authorId,
                                 @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
                                 LocalDateTime createdAt) {
}
