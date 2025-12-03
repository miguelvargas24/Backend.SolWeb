package backend.com.backend.service;

import backend.com.backend.model.Inventory;
import backend.com.backend.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    public List<Inventory> getAllInventory() {
        return inventoryRepository.findAll();
    }

    public Page<Inventory> getActiveInventory(Pageable pageable) {
        return inventoryRepository.findByActiveTrueOrderByExpirationDateAsc(pageable);
    }

    public Optional<Inventory> getInventoryById(Long id) {
        return inventoryRepository.findById(id);
    }

    public Optional<Inventory> getInventoryByFoodItemAndBatch(Long foodItemId, String batchNumber) {
        return inventoryRepository.findByFoodItem_IdFoodItemAndBatchNumber(foodItemId, batchNumber);
    }

    public List<Inventory> getInventoryByFoodItem(Long foodItemId) {
        return inventoryRepository.findByFoodItem_IdFoodItemAndActiveTrue(foodItemId);
    }

    public List<Inventory> getExpiredItems(LocalDate date) {
        return inventoryRepository.findByExpirationDateBeforeAndActiveTrue(date);
    }

    public Page<Inventory> getInventoryByCategory(backend.com.backend.model.FoodItem.FoodCategory category,
            Pageable pageable) {
        return inventoryRepository.findByCategoryAndActiveTrue(category, pageable);
    }

    public Page<Inventory> getInventoryOrderedByQuantity(Pageable pageable) {
        return inventoryRepository.findAllActiveOrderByQuantity(pageable);
    }

    /**
     * Agregar stock al inventario (usado al registrar donación)
     */
    public Inventory addStock(Inventory inventory) {
        Optional<Inventory> existingInventory = inventoryRepository
                .findByFoodItem_IdFoodItemAndBatchNumber(
                        inventory.getFoodItem().getIdFoodItem(),
                        inventory.getBatchNumber());

        if (existingInventory.isPresent()) {
            // Si ya existe ese lote, sumamos la cantidad
            Inventory existing = existingInventory.get();
            existing.addQuantity(inventory.getCurrentQuantity());
            return inventoryRepository.save(existing);
        } else {
            // Si es nuevo lote, lo creamos
            inventory.setActive(true);
            inventory.setInitialQuantity(inventory.getCurrentQuantity());
            return inventoryRepository.save(inventory);
        }
    }

    /**
     * Restar stock del inventario (usado al registrar distribución)
     */
    public Inventory subtractStock(Long foodItemId, String batchNumber, BigDecimal quantity) {
        Inventory inventory = inventoryRepository
                .findByFoodItem_IdFoodItemAndBatchNumber(foodItemId, batchNumber)
                .orElseThrow(() -> new RuntimeException("Inventory not found for batch: " + batchNumber));

        if (inventory.getCurrentQuantity().compareTo(quantity) < 0) {
            throw new RuntimeException(
                    "Insufficient stock. Available: " + inventory.getCurrentQuantity() + ", Required: " + quantity);
        }

        inventory.subtractQuantity(quantity);
        return inventoryRepository.save(inventory);
    }

    public Inventory updateInventory(Long id, Inventory inventoryDetails) {
        return inventoryRepository.findById(id)
                .map(inventory -> {
                    inventory.setLocation(inventoryDetails.getLocation());
                    inventory.setExpirationDate(inventoryDetails.getExpirationDate());
                    return inventoryRepository.save(inventory);
                })
                .orElseThrow(() -> new RuntimeException("Inventory not found with id: " + id));
    }
}
