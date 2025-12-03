package backend.com.backend.controller;

import backend.com.backend.model.Beneficiary;
import backend.com.backend.service.BeneficiaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/beneficiaries")
@CrossOrigin(origins = "*")
public class BeneficiaryController {

    @Autowired
    private BeneficiaryService beneficiaryService;

    @GetMapping
    public ResponseEntity<List<Beneficiary>> getAllBeneficiaries() {
        return ResponseEntity.ok(beneficiaryService.getAllBeneficiaries());
    }

    @GetMapping("/paginated")
    public ResponseEntity<Page<Beneficiary>> getActiveBeneficiaries(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "idBeneficiary") String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        return ResponseEntity.ok(beneficiaryService.getActiveBeneficiaries(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Beneficiary> getBeneficiaryById(@PathVariable Long id) {
        return beneficiaryService.getBeneficiaryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/document/{documentNumber}")
    public ResponseEntity<Beneficiary> getBeneficiaryByDocument(@PathVariable String documentNumber) {
        return beneficiaryService.getBeneficiaryByDocumentNumber(documentNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Beneficiary>> searchBeneficiaries(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(beneficiaryService.searchBeneficiariesByName(name, pageable));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<Page<Beneficiary>> getBeneficiariesByType(
            @PathVariable Beneficiary.BeneficiaryType type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(beneficiaryService.getBeneficiariesByType(type, pageable));
    }

    @PostMapping
    public ResponseEntity<Beneficiary> createBeneficiary(@RequestBody Beneficiary beneficiary) {
        return ResponseEntity.status(HttpStatus.CREATED).body(beneficiaryService.createBeneficiary(beneficiary));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Beneficiary> updateBeneficiary(@PathVariable Long id, @RequestBody Beneficiary beneficiary) {
        try {
            return ResponseEntity.ok(beneficiaryService.updateBeneficiary(id, beneficiary));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBeneficiary(@PathVariable Long id) {
        try {
            beneficiaryService.deleteBeneficiary(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
