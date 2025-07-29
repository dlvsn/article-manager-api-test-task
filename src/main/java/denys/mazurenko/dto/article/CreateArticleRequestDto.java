package denys.mazurenko.dto.article;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record CreateArticleRequestDto(@NotBlank(message = "Please, add title")
                                      String title,
                                      @NotBlank(message = "Please, add content")
                                      @Length(max = 100, message = "Content length should be less than 100 symbols")
                                      String content) {
}
