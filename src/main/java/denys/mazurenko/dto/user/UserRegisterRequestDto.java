package denys.mazurenko.dto.user;

import jakarta.validation.constraints.NotBlank;

public record UserRegisterRequestDto(@NotBlank(message = "Please, enter email")
                                     String email,
                                     @NotBlank(message = "Please, enter password")
                                     String password,
                                     @NotBlank(message = "Please, repeat password")
                                     String repeatPassword) {
}
