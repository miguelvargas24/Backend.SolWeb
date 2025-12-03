package backend.com.backend.service;

import backend.com.backend.model.Donor;
import backend.com.backend.repository.DonorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DonorService {

    @Autowired
    private DonorRepository donorRepository;

    @Transactional(readOnly = true)
    public List<Donor> getAllDonors() {
        return donorRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Page<Donor> getActiveDonors(Pageable pageable) {
        return donorRepository.findByActiveTrue(pageable);
    }

    @Transactional(readOnly = true)
    public Optional<Donor> getDonorById(Long id) {
        return donorRepository.findById(id);
    }

    public Optional<Donor> getDonorByRuc(String ruc) {
        return donorRepository.findByRuc(ruc);
    }

    public Page<Donor> searchDonorsByName(String businessName, Pageable pageable) {
        return donorRepository.findByBusinessNameContainingIgnoreCaseAndActiveTrue(businessName, pageable);
    }

    public Donor createDonor(Donor donor) {
        donor.setActive(true);
        return donorRepository.save(donor);
    }

    public Donor updateDonor(Long id, Donor donorDetails) {
        return donorRepository.findById(id)
                .map(donor -> {
                    donor.setRuc(donorDetails.getRuc());
                    donor.setBusinessName(donorDetails.getBusinessName());
                    donor.setAddress(donorDetails.getAddress());
                    donor.setContactName(donorDetails.getContactName());
                    donor.setContactPhone(donorDetails.getContactPhone());
                    donor.setContactEmail(donorDetails.getContactEmail());
                    donor.setActive(donorDetails.getActive());
                    return donorRepository.save(donor);
                })
                .orElseThrow(() -> new RuntimeException("Donor not found with id: " + id));
    }

    public void deleteDonor(Long id) {
        donorRepository.findById(id)
                .map(donor -> {
                    donor.setActive(false);
                    return donorRepository.save(donor);
                })
                .orElseThrow(() -> new RuntimeException("Donor not found with id: " + id));
    }
}
