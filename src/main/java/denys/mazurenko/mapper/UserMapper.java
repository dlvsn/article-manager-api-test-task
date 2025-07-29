package denys.mazurenko.mapper;

import denys.mazurenko.config.MapperConfig;
import denys.mazurenko.dto.user.UserRegisterRequestDto;
import denys.mazurenko.entity.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    User toEntity(UserRegisterRequestDto dto);
}
