package denys.mazurenko.entity;

import java.util.Set;

public class User {
    private Long id;
    private String username;
    private String password;
    private Set<Role> roles;
}
