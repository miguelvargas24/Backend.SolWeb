package backend.com.backend.service;

import backend.com.backend.model.*;
import backend.com.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DonationService {

    @Autowired
    private DonationRepository donationRepository;

    @Autowired
    private DonationDetailRepository donationDetailRepository;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private DonorRepository donorRepository;

    @Autowired
    private FoodItemRepository foodItemRepository;

    @Autowired
    private UserRepository userRepository;

    public Page<Donation> getAllDonations(Pageable pageable) {
        return donationRepository.findAll(pageable);
    }

    public Optional<Donation> getDonationById(Long id) {
        return donationRepository.findById(id);
    }

    public Optional<Donation> getDonationByCode(String code) {
        return donationRepository.findByDonationCode(code);
    }

    public Page<Donation> getDonationsByDateRange(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        return donationRepository.findByDonationDateBetween(startDate, endDate, pageable);
    }

    public Page<Donation> getDonationsByDonor(Long donorId, Pageable pageable) {
        return donationRepository.findByDonor_IdDonor(donorId, pageable);
    }

    public Page<Donation> getDonationsByStatus(Donation.DonationStatus status, Pageable pageable) {
        return donationRepository.findByStatus(status, pageable);
    }

    /**
     * Crear donación con sus detalles y actualizar inventario automáticamente
     */
    public Donation createDonation(Donation donation) {
        // Validar y cargar relaciones
        Donor donor = donorRepository.findById(donation.getDonor().getIdDonor())
                .orElseThrow(() -> new RuntimeException("Donor not found"));

        User receivedBy = userRepository.findById(donation.getReceivedBy().getIdUser())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Generar código único
        donation.setDonationCode(generateDonationCode());
        donation.setDonor(donor);
        donation.setReceivedBy(receivedBy);
        donation.setStatus(Donation.DonationStatus.RECEIVED);

        // Guardar donación
        Donation savedDonation = donationRepository.save(donation);

        // Procesar detalles y actualizar inventario
        for (DonationDetail detail : donation.getDetails()) {
            detail.setDonation(savedDonation);

            FoodItem foodItem = foodItemRepository.findById(detail.getFoodItem().getIdFoodItem())
                    .orElseThrow(() -> new RuntimeException("Food item not found"));
            detail.setFoodItem(foodItem);

            donationDetailRepository.save(detail);

            // Actualizar inventario (SUMA)
            Inventory inventory = new Inventory();
            inventory.setFoodItem(foodItem);
            inventory.setBatchNumber(detail.getBatchNumber());
            inventory.setCurrentQuantity(detail.getQuantity());
            inventory.setInitialQuantity(detail.getQuantity());
            inventory.setExpirationDate(detail.getExpirationDate());
            inventory.setEntryDate(donation.getDonationDate());
            inventory.setLastMovementDate(LocalDateTime.now());
            inventory.setDonor(donor);
            inventory.setActive(true);

            inventoryService.addStock(inventory);
        }

        return savedDonation;
    }

    public Donation updateDonationStatus(Long id, Donation.DonationStatus status) {
        return donationRepository.findById(id)
                .map(donation -> {
                    donation.setStatus(status);
                    return donationRepository.save(donation);
                })
                .orElseThrow(() -> new RuntimeException("Donation not found with id: " + id));
    }

    public List<DonationDetail> getDonationDetails(Long donationId) {
        return donationDetailRepository.findByDonation_IdDonation(donationId);
    }

    /**
     * Genera código único para donación: DON-2025-0001
     */
    private String generateDonationCode() {
        int year = Year.now().getValue();
        long count = donationRepository.count() + 1;
        return String.format("DON-%d-%04d", year, count);
    }
}
