package denys.mazurenko.security;

import denys.mazurenko.dto.user.UserAuthenticatedResponseDto;
import denys.mazurenko.dto.user.UserLoginRequestDto;
import denys.mazurenko.dto.user.UserRegisterRequestDto;
import denys.mazurenko.dto.user.UserRegisteredResponseDto;
import denys.mazurenko.exception.RegistrationFailedException;

public interface AuthService {
    UserAuthenticatedResponseDto authenticate(UserLoginRequestDto userLoginRequestDto);

    UserRegisteredResponseDto register(UserRegisterRequestDto userRegisterRequestDto) throws RegistrationFailedException;
}
