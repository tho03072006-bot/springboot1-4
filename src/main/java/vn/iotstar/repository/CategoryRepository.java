package vn.iotstar.repository;
import vn.iotstar.entity.Category;
import org.springframework.data.jpa.repository.*;
import java.util.Optional;
public interface CategoryRepository extends JpaRepository<Category,Long>, JpaSpecificationExecutor<Category> {
    Optional<Category> findByNameIgnoreCase(String value);
    boolean existsByNameIgnoreCaseAndIdNot(String value, Long id);
    
}
