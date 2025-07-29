package denys.mazurenko.controller;

import denys.mazurenko.dto.user.UserAuthenticatedResponseDto;
import denys.mazurenko.dto.user.UserLoginRequestDto;
import denys.mazurenko.dto.user.UserRegisterRequestDto;
import denys.mazurenko.dto.user.UserRegisteredResponseDto;
import denys.mazurenko.exception.RegistrationFailedException;
import denys.mazurenko.security.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public UserAuthenticatedResponseDto login(@RequestBody
                                              @Valid
                                              UserLoginRequestDto userLoginRequestDto) {
        return authService.authenticate(userLoginRequestDto);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserRegisteredResponseDto register(@RequestBody
                                              @Valid
                                              UserRegisterRequestDto userDto)
            throws RegistrationFailedException {
        return authService.register(userDto);
    }
}
