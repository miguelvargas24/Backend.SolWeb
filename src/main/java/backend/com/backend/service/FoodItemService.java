package backend.com.backend.service;

import backend.com.backend.model.FoodItem;
import backend.com.backend.repository.FoodItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FoodItemService {

    @Autowired
    private FoodItemRepository foodItemRepository;

    @Transactional(readOnly = true)
    public List<FoodItem> getAllFoodItems() {
        return foodItemRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Page<FoodItem> getActiveFoodItems(Pageable pageable) {
        return foodItemRepository.findByActiveTrue(pageable);
    }

    @Transactional(readOnly = true)
    public Optional<FoodItem> getFoodItemById(Long id) {
        return foodItemRepository.findById(id);
    }

    public Optional<FoodItem> getFoodItemByName(String name) {
        return foodItemRepository.findByName(name);
    }

    public Page<FoodItem> searchFoodItemsByName(String name, Pageable pageable) {
        return foodItemRepository.findByNameContainingIgnoreCaseAndActiveTrue(name, pageable);
    }

    public Page<FoodItem> getFoodItemsByCategory(FoodItem.FoodCategory category, Pageable pageable) {
        return foodItemRepository.findByCategoryAndActiveTrue(category, pageable);
    }

    public FoodItem createFoodItem(FoodItem foodItem) {
        foodItem.setActive(true);
        return foodItemRepository.save(foodItem);
    }

    public FoodItem updateFoodItem(Long id, FoodItem foodItemDetails) {
        return foodItemRepository.findById(id)
                .map(foodItem -> {
                    foodItem.setName(foodItemDetails.getName());
                    foodItem.setDescription(foodItemDetails.getDescription());
                    foodItem.setUnitOfMeasure(foodItemDetails.getUnitOfMeasure());
                    foodItem.setCategory(foodItemDetails.getCategory());
                    foodItem.setActive(foodItemDetails.getActive());
                    return foodItemRepository.save(foodItem);
                })
                .orElseThrow(() -> new RuntimeException("FoodItem not found with id: " + id));
    }

    public void deleteFoodItem(Long id) {
        foodItemRepository.findById(id)
                .map(foodItem -> {
                    foodItem.setActive(false);
                    return foodItemRepository.save(foodItem);
                })
                .orElseThrow(() -> new RuntimeException("FoodItem not found with id: " + id));
    }
}
