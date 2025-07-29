package denys.mazurenko.security;

import denys.mazurenko.entity.User;
import denys.mazurenko.exception.EntityNotFoundException;
import denys.mazurenko.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsServiceImpl implements CustomUserDetailsService {
    private final UserRepository userRepository;

    @Override
    public User loadUserByAuthentication(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return findUserByUserName(user.getUsername());
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        return findUserByUserName(username);
    }

    private User findUserByUserName(String username) {
        return userRepository.findUserByEmail(username)
                .orElseThrow(() ->
                        new EntityNotFoundException("Can't find user with username: " + username)
                );
    }
}
