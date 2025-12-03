package backend.com.backend.repository;

import backend.com.backend.model.Inventory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    Optional<Inventory> findByFoodItem_IdFoodItemAndBatchNumber(Long foodItemId, String batchNumber);

    Page<Inventory> findByActiveTrueOrderByExpirationDateAsc(Pageable pageable);

    List<Inventory> findByFoodItem_IdFoodItemAndActiveTrue(Long foodItemId);

    List<Inventory> findByExpirationDateBeforeAndActiveTrue(LocalDate date);

    @Query("SELECT i FROM Inventory i WHERE i.foodItem.category = :category AND i.active = true ORDER BY i.expirationDate ASC")
    Page<Inventory> findByCategoryAndActiveTrue(backend.com.backend.model.FoodItem.FoodCategory category,
            Pageable pageable);

    @Query("SELECT i FROM Inventory i WHERE i.active = true ORDER BY i.currentQuantity DESC")
    Page<Inventory> findAllActiveOrderByQuantity(Pageable pageable);
}
