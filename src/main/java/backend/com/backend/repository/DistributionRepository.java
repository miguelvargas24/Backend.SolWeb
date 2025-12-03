package backend.com.backend.repository;

import backend.com.backend.model.Distribution;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface DistributionRepository extends JpaRepository<Distribution, Long> {

    Optional<Distribution> findByDistributionCode(String distributionCode);

    Page<Distribution> findByDistributionDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);

    Page<Distribution> findByBeneficiary_IdBeneficiary(Long beneficiaryId, Pageable pageable);

    Page<Distribution> findByStatus(Distribution.DistributionStatus status, Pageable pageable);
}
