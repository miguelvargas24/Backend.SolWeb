package backend.com.backend.service;

import backend.com.backend.model.Beneficiary;
import backend.com.backend.repository.BeneficiaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BeneficiaryService {

    @Autowired
    private BeneficiaryRepository beneficiaryRepository;

    @Transactional(readOnly = true)
    public List<Beneficiary> getAllBeneficiaries() {
        return beneficiaryRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Page<Beneficiary> getActiveBeneficiaries(Pageable pageable) {
        return beneficiaryRepository.findByActiveTrue(pageable);
    }

    @Transactional(readOnly = true)
    public Optional<Beneficiary> getBeneficiaryById(Long id) {
        return beneficiaryRepository.findById(id);
    }

    public Optional<Beneficiary> getBeneficiaryByDocumentNumber(String documentNumber) {
        return beneficiaryRepository.findByDocumentNumber(documentNumber);
    }

    public Page<Beneficiary> searchBeneficiariesByName(String name, Pageable pageable) {
        return beneficiaryRepository.findByNameContainingIgnoreCaseAndActiveTrue(name, pageable);
    }

    public Page<Beneficiary> getBeneficiariesByType(Beneficiary.BeneficiaryType type, Pageable pageable) {
        return beneficiaryRepository.findByTypeAndActiveTrue(type, pageable);
    }

    public Beneficiary createBeneficiary(Beneficiary beneficiary) {
        beneficiary.setActive(true);
        return beneficiaryRepository.save(beneficiary);
    }

    public Beneficiary updateBeneficiary(Long id, Beneficiary beneficiaryDetails) {
        return beneficiaryRepository.findById(id)
                .map(beneficiary -> {
                    beneficiary.setType(beneficiaryDetails.getType());
                    beneficiary.setDocumentNumber(beneficiaryDetails.getDocumentNumber());
                    beneficiary.setName(beneficiaryDetails.getName());
                    beneficiary.setAddress(beneficiaryDetails.getAddress());
                    beneficiary.setPhone(beneficiaryDetails.getPhone());
                    beneficiary.setEmail(beneficiaryDetails.getEmail());
                    beneficiary.setRepresentativeName(beneficiaryDetails.getRepresentativeName());
                    beneficiary.setDescription(beneficiaryDetails.getDescription());
                    beneficiary.setActive(beneficiaryDetails.getActive());
                    return beneficiaryRepository.save(beneficiary);
                })
                .orElseThrow(() -> new RuntimeException("Beneficiary not found with id: " + id));
    }

    public void deleteBeneficiary(Long id) {
        beneficiaryRepository.findById(id)
                .map(beneficiary -> {
                    beneficiary.setActive(false);
                    return beneficiaryRepository.save(beneficiary);
                })
                .orElseThrow(() -> new RuntimeException("Beneficiary not found with id: " + id));
    }
}
