package denys.mazurenko.util;

import denys.mazurenko.dto.author.MostActiveAuthorsResponseDto;
import java.util.List;

public class StaticObjectFactory {
    public static List<MostActiveAuthorsResponseDto> initExpectedList() {
        return List.of(new MostActiveAuthorsResponseDto(2L, 3L),
                new MostActiveAuthorsResponseDto(3L, 2L),
                new MostActiveAuthorsResponseDto(4L, 1L));
    }
}
