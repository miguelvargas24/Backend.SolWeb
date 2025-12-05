package backend.com.backend.controller;

import backend.com.backend.dto.DonationRequest;
import backend.com.backend.model.Donation;
import backend.com.backend.model.DonationDetail;
import backend.com.backend.service.DonationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/donations")
@CrossOrigin(origins = "*")
public class DonationController {

    @Autowired
    private DonationService donationService;

    @GetMapping
    public ResponseEntity<Page<Donation>> getAllDonations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "idDonation") String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        return ResponseEntity.ok(donationService.getAllDonations(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Donation> getDonationById(@PathVariable Long id) {
        return donationService.getDonationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<Donation> getDonationByCode(@PathVariable String code) {
        return donationService.getDonationByCode(code)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/date-range")
    public ResponseEntity<Page<Donation>> getDonationsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("donationDate").descending());
        return ResponseEntity.ok(donationService.getDonationsByDateRange(startDate, endDate, pageable));
    }

    @GetMapping("/donor/{donorId}")
    public ResponseEntity<Page<Donation>> getDonationsByDonor(
            @PathVariable Long donorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("donationDate").descending());
        return ResponseEntity.ok(donationService.getDonationsByDonor(donorId, pageable));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<Page<Donation>> getDonationsByStatus(
            @PathVariable Donation.DonationStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("donationDate").descending());
        return ResponseEntity.ok(donationService.getDonationsByStatus(status, pageable));
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<List<DonationDetail>> getDonationDetails(@PathVariable Long id) {
        return ResponseEntity.ok(donationService.getDonationDetails(id));
    }

    @PostMapping
    public ResponseEntity<?> createDonation(@RequestBody DonationRequest request) {
        try {
            System.out.println("Recibiendo donación: " + request);
            Donation donation = donationService.createDonation(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(donation);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("error", "Bad Request", "message", e.getMessage()));
        }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Donation> updateDonationStatus(
            @PathVariable Long id,
            @RequestParam Donation.DonationStatus status) {
        try {
            return ResponseEntity.ok(donationService.updateDonationStatus(id, status));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
