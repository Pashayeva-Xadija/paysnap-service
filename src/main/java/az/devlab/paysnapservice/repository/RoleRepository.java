package az.devlab.paysnapservice.repository;

import az.devlab.paysnapservice.enums.RoleName;
import az.devlab.paysnapservice.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(RoleName name);
}
