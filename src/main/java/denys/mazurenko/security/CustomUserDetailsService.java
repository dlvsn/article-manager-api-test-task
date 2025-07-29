package denys.mazurenko.security;

import denys.mazurenko.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface CustomUserDetailsService extends UserDetailsService {
    User loadUserByAuthentication(Authentication authentication) throws UsernameNotFoundException;
}
