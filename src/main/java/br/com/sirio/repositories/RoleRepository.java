package br.com.sirio.repositories;

import br.com.sirio.models.Role;
import br.com.sirio.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(Role.RoleName roleName);

}
