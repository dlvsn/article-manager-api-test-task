package denys.mazurenko.entity;

import java.time.LocalDateTime;

public class Article {
    private Long id;
    private User author;
    private String title;
    private String content;
    private LocalDateTime createdAt;
}
