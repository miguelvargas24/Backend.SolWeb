package backend.com.backend.controller;

import backend.com.backend.model.FoodItem;
import backend.com.backend.model.Inventory;
import backend.com.backend.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@CrossOrigin(origins = "*")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @GetMapping
    public ResponseEntity<List<Inventory>> getAllInventory() {
        return ResponseEntity.ok(inventoryService.getAllInventory());
    }

    @GetMapping("/active")
    public ResponseEntity<Page<Inventory>> getActiveInventory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(inventoryService.getActiveInventory(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Inventory> getInventoryById(@PathVariable Long id) {
        return inventoryService.getInventoryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/food-item/{foodItemId}")
    public ResponseEntity<List<Inventory>> getInventoryByFoodItem(@PathVariable Long foodItemId) {
        return ResponseEntity.ok(inventoryService.getInventoryByFoodItem(foodItemId));
    }

    @GetMapping("/food-item/{foodItemId}/batch/{batchNumber}")
    public ResponseEntity<Inventory> getInventoryByFoodItemAndBatch(
            @PathVariable Long foodItemId,
            @PathVariable String batchNumber) {
        return inventoryService.getInventoryByFoodItemAndBatch(foodItemId, batchNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/expired")
    public ResponseEntity<List<Inventory>> getExpiredItems(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        LocalDate checkDate = date != null ? date : LocalDate.now();
        return ResponseEntity.ok(inventoryService.getExpiredItems(checkDate));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<Page<Inventory>> getInventoryByCategory(
            @PathVariable FoodItem.FoodCategory category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(inventoryService.getInventoryByCategory(category, pageable));
    }

    @GetMapping("/ordered-by-quantity")
    public ResponseEntity<Page<Inventory>> getInventoryOrderedByQuantity(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(inventoryService.getInventoryOrderedByQuantity(pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Inventory> updateInventory(@PathVariable Long id, @RequestBody Inventory inventory) {
        try {
            return ResponseEntity.ok(inventoryService.updateInventory(id, inventory));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
