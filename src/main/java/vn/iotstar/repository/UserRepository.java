package vn.iotstar.repository;
import vn.iotstar.entity.AppUser;
import org.springframework.data.jpa.repository.*;
import java.util.Optional;
public interface UserRepository extends JpaRepository<AppUser,Long>, JpaSpecificationExecutor<AppUser> {
    Optional<AppUser> findByUsernameIgnoreCase(String value);
    boolean existsByUsernameIgnoreCaseAndIdNot(String value, Long id);
    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);
}
