package denys.mazurenko.repository;

import denys.mazurenko.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Set<Role> findRolesByNameIn(Set<Role.Roles> roleNames);
}
