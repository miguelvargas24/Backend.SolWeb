package backend.com.backend.controller;

import backend.com.backend.model.Distribution;
import backend.com.backend.model.DistributionDetail;
import backend.com.backend.service.DistributionService;
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

@RestController
@RequestMapping("/api/distributions")
@CrossOrigin(origins = "*")
public class DistributionController {

    @Autowired
    private DistributionService distributionService;

    @GetMapping
    public ResponseEntity<Page<Distribution>> getAllDistributions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "idDistribution") String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        return ResponseEntity.ok(distributionService.getAllDistributions(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Distribution> getDistributionById(@PathVariable Long id) {
        return distributionService.getDistributionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<Distribution> getDistributionByCode(@PathVariable String code) {
        return distributionService.getDistributionByCode(code)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/date-range")
    public ResponseEntity<Page<Distribution>> getDistributionsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("distributionDate").descending());
        return ResponseEntity.ok(distributionService.getDistributionsByDateRange(startDate, endDate, pageable));
    }

    @GetMapping("/beneficiary/{beneficiaryId}")
    public ResponseEntity<Page<Distribution>> getDistributionsByBeneficiary(
            @PathVariable Long beneficiaryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("distributionDate").descending());
        return ResponseEntity.ok(distributionService.getDistributionsByBeneficiary(beneficiaryId, pageable));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<Page<Distribution>> getDistributionsByStatus(
            @PathVariable Distribution.DistributionStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("distributionDate").descending());
        return ResponseEntity.ok(distributionService.getDistributionsByStatus(status, pageable));
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<List<DistributionDetail>> getDistributionDetails(@PathVariable Long id) {
        return ResponseEntity.ok(distributionService.getDistributionDetails(id));
    }

    @PostMapping
    public ResponseEntity<Distribution> createDistribution(@RequestBody Distribution distribution) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(distributionService.createDistribution(distribution));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Distribution> updateDistributionStatus(
            @PathVariable Long id,
            @RequestParam Distribution.DistributionStatus status) {
        try {
            return ResponseEntity.ok(distributionService.updateDistributionStatus(id, status));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
