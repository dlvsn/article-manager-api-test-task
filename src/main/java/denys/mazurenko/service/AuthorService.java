package denys.mazurenko.service;

import denys.mazurenko.dto.author.MostActiveAuthorsResponseDto;
import java.util.List;

public interface AuthorService {
    List<MostActiveAuthorsResponseDto> getAllMostActiveAuthors();
}
