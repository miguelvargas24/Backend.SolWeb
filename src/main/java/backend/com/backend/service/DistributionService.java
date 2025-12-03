package backend.com.backend.service;

import backend.com.backend.model.*;
import backend.com.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DistributionService {

    @Autowired
    private DistributionRepository distributionRepository;

    @Autowired
    private DistributionDetailRepository distributionDetailRepository;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private BeneficiaryRepository beneficiaryRepository;

    @Autowired
    private FoodItemRepository foodItemRepository;

    @Autowired
    private UserRepository userRepository;

    public Page<Distribution> getAllDistributions(Pageable pageable) {
        return distributionRepository.findAll(pageable);
    }

    public Optional<Distribution> getDistributionById(Long id) {
        return distributionRepository.findById(id);
    }

    public Optional<Distribution> getDistributionByCode(String code) {
        return distributionRepository.findByDistributionCode(code);
    }

    public Page<Distribution> getDistributionsByDateRange(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        return distributionRepository.findByDistributionDateBetween(startDate, endDate, pageable);
    }

    public Page<Distribution> getDistributionsByBeneficiary(Long beneficiaryId, Pageable pageable) {
        return distributionRepository.findByBeneficiary_IdBeneficiary(beneficiaryId, pageable);
    }

    public Page<Distribution> getDistributionsByStatus(Distribution.DistributionStatus status, Pageable pageable) {
        return distributionRepository.findByStatus(status, pageable);
    }

    /**
     * Crear distribución con sus detalles y actualizar inventario automáticamente
     */
    public Distribution createDistribution(Distribution distribution) {
        // Validar y cargar relaciones
        Beneficiary beneficiary = beneficiaryRepository.findById(distribution.getBeneficiary().getIdBeneficiary())
                .orElseThrow(() -> new RuntimeException("Beneficiary not found"));

        User deliveredBy = userRepository.findById(distribution.getDeliveredBy().getIdUser())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Generar código único
        distribution.setDistributionCode(generateDistributionCode());
        distribution.setBeneficiary(beneficiary);
        distribution.setDeliveredBy(deliveredBy);
        distribution.setStatus(Distribution.DistributionStatus.DELIVERED);

        // Guardar distribución
        Distribution savedDistribution = distributionRepository.save(distribution);

        // Procesar detalles y actualizar inventario
        for (DistributionDetail detail : distribution.getDetails()) {
            detail.setDistribution(savedDistribution);

            FoodItem foodItem = foodItemRepository.findById(detail.getFoodItem().getIdFoodItem())
                    .orElseThrow(() -> new RuntimeException("Food item not found"));
            detail.setFoodItem(foodItem);

            distributionDetailRepository.save(detail);

            // Actualizar inventario (RESTA)
            inventoryService.subtractStock(
                    foodItem.getIdFoodItem(),
                    detail.getBatchNumber(),
                    detail.getQuantity());
        }

        return savedDistribution;
    }

    public Distribution updateDistributionStatus(Long id, Distribution.DistributionStatus status) {
        return distributionRepository.findById(id)
                .map(distribution -> {
                    distribution.setStatus(status);
                    return distributionRepository.save(distribution);
                })
                .orElseThrow(() -> new RuntimeException("Distribution not found with id: " + id));
    }

    public List<DistributionDetail> getDistributionDetails(Long distributionId) {
        return distributionDetailRepository.findByDistribution_IdDistribution(distributionId);
    }

    /**
     * Genera código único para distribución: DIS-2025-0001
     */
    private String generateDistributionCode() {
        int year = Year.now().getValue();
        long count = distributionRepository.count() + 1;
        return String.format("DIS-%d-%04d", year, count);
    }
}
