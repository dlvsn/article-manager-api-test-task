package denys.mazurenko.service;

import denys.mazurenko.dto.author.MostActiveAuthorsResponseDto;
import denys.mazurenko.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {
    private static final Long LAST_DAYS = 50L;
    private static final int PAGE_NUMBER = 0;
    private static final int PAGE_SIZE = 3;
    private final ArticleRepository articleRepository;

    @Override
    public List<MostActiveAuthorsResponseDto> getAllMostActiveAuthors() {
        return articleRepository
                .findMostActiveAuthors(LocalDateTime.now().minusDays(LAST_DAYS),
                        PageRequest.of(PAGE_NUMBER, PAGE_SIZE));
    }
}
