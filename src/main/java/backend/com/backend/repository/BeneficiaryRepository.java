package backend.com.backend.repository;

import backend.com.backend.model.Beneficiary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BeneficiaryRepository extends JpaRepository<Beneficiary, Long> {

    Optional<Beneficiary> findByDocumentNumber(String documentNumber);

    Page<Beneficiary> findByActiveTrue(Pageable pageable);

    Page<Beneficiary> findByNameContainingIgnoreCaseAndActiveTrue(String name, Pageable pageable);

    Page<Beneficiary> findByTypeAndActiveTrue(Beneficiary.BeneficiaryType type, Pageable pageable);
}
