package backend.com.backend.service;

import backend.com.backend.dto.DonationRequest;
import backend.com.backend.model.*;
import backend.com.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.ArrayList;
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
     * Crear donación desde DTO (usado por el frontend)
     */
    public Donation createDonation(DonationRequest request) {
        System.out.println("=== Creando donación ===");
        System.out.println("Request: " + request);
        System.out.println("Usuario recibido (receivedBy): " + request.getReceivedBy());

        // Validar y cargar relaciones
        Donor donor = donorRepository.findById(request.getDonor())
                .orElseThrow(() -> new RuntimeException("Donante no encontrado con id: " + request.getDonor()));

        System.out.println("Verificando si existe usuario con ID: " + request.getReceivedBy());
        System.out.println("Total de usuarios en DB: " + userRepository.count());

        // Listar todos los IDs de usuarios existentes para debugging
        userRepository.findAll().forEach(u -> System.out.println(
                "Usuario en DB - ID: " + u.getIdUser() + ", Email: " + u.getEmail() + ", Nombre: " + u.getName()));

        User receivedBy = userRepository.findById(request.getReceivedBy())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + request.getReceivedBy()
                        + ". Los usuarios disponibles tienen IDs diferentes. Verifique que el usuario existe en la base de datos."));

        System.out.println("Donante encontrado: " + donor.getBusinessName());
        System.out.println("Usuario encontrado: " + receivedBy.getName() + " (ID: " + receivedBy.getIdUser() + ")");

        // Crear entidad Donation
        Donation donation = new Donation();
        donation.setDonationCode(generateDonationCode());
        donation.setDonor(donor);
        donation.setReceivedBy(receivedBy);
        donation.setDonationDate(request.getDonationDate());
        donation.setObservations(request.getObservations());
        donation.setStatus(Donation.DonationStatus.RECEIVED);

        // Guardar donación
        Donation savedDonation = donationRepository.save(donation);

        // Procesar detalles y actualizar inventario
        List<DonationDetail> details = new ArrayList<>();
        for (DonationRequest.DonationDetailRequest detailRequest : request.getDetails()) {
            FoodItem foodItem = foodItemRepository.findById(detailRequest.getFoodItem())
                    .orElseThrow(
                            () -> new RuntimeException("Food item not found with id: " + detailRequest.getFoodItem()));

            DonationDetail detail = new DonationDetail();
            detail.setDonation(savedDonation);
            detail.setFoodItem(foodItem);
            detail.setQuantity(BigDecimal.valueOf(detailRequest.getQuantity()));
            detail.setBatchNumber(detailRequest.getBatchNumber());
            detail.setExpirationDate(detailRequest.getExpirationDate());
            detail.setObservations(detailRequest.getObservations());

            DonationDetail savedDetail = donationDetailRepository.save(detail);
            details.add(savedDetail);

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

        savedDonation.setDetails(details);
        return savedDonation;
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
