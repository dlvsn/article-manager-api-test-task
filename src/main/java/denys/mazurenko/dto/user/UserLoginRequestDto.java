package denys.mazurenko.dto.user;

import jakarta.validation.constraints.NotBlank;

public record UserLoginRequestDto(@NotBlank(message = "Please, enter email")
                                  String email,
                                  @NotBlank(message = "Please, enter password")
                                  String password) {
}
