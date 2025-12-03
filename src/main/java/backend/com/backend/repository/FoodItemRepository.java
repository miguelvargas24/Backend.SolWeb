package backend.com.backend.repository;

import backend.com.backend.model.FoodItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FoodItemRepository extends JpaRepository<FoodItem, Long> {

    Optional<FoodItem> findByName(String name);

    Page<FoodItem> findByActiveTrue(Pageable pageable);

    Page<FoodItem> findByNameContainingIgnoreCaseAndActiveTrue(String name, Pageable pageable);

    Page<FoodItem> findByCategoryAndActiveTrue(FoodItem.FoodCategory category, Pageable pageable);
}
