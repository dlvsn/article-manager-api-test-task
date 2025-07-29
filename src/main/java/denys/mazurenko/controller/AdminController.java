package denys.mazurenko.controller;

import denys.mazurenko.dto.author.MostActiveAuthorsResponseDto;
import denys.mazurenko.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final AuthorService authorService;

    @GetMapping("/statistics/authors/top-active")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<MostActiveAuthorsResponseDto> findMostActiveAuthors() {
        return authorService.getAllMostActiveAuthors();
    }
}
