package denys.mazurenko.security;

import denys.mazurenko.dto.user.UserAuthenticatedResponseDto;
import denys.mazurenko.dto.user.UserLoginRequestDto;
import denys.mazurenko.dto.user.UserRegisterRequestDto;
import denys.mazurenko.dto.user.UserRegisteredResponseDto;
import denys.mazurenko.entity.Role;
import denys.mazurenko.entity.User;
import denys.mazurenko.exception.EntityNotFoundException;
import denys.mazurenko.exception.RegistrationFailedException;
import denys.mazurenko.mapper.UserMapper;
import denys.mazurenko.repository.RoleRepository;
import denys.mazurenko.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Set;

@Transactional
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;

    @Override
    public UserAuthenticatedResponseDto authenticate(UserLoginRequestDto userLoginRequestDto) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userLoginRequestDto.email(),
                        userLoginRequestDto.password())
        );
        String generatedToken = jwtUtil.generateToken(authenticate.getName());
        return new UserAuthenticatedResponseDto(generatedToken);
    }

    @Override
    public UserRegisteredResponseDto register(UserRegisterRequestDto userRegisterRequestDto)
            throws RegistrationFailedException {
        if (isUserExists(userRegisterRequestDto.email())) {
            throw new RegistrationFailedException("User with email "
                    + userRegisterRequestDto.email()
                    + " already exists");
        }
        User newUser = userMapper.toEntity(userRegisterRequestDto);
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        newUser.setRoles(findRoles());
        userRepository.save(newUser);
        return new UserRegisteredResponseDto(newUser.getUsername());
    }

    private boolean isUserExists(String username) {
        return userRepository.findUserByEmail(username).isPresent();
    }

    private Set<Role> findRoles() {
        Set<Role.Roles> roles = Set.of(Role.Roles.ROLE_USER, Role.Roles.ROLE_PUBLISHER);
        Set<Role> rolesByNameIn = roleRepository.findRolesByNameIn(roles);
        if (rolesByNameIn.isEmpty()) {
            throw new EntityNotFoundException("Can't find roles by given names " + roles);
        }
        return rolesByNameIn;
    }
}
