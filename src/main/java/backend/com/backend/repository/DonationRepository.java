package backend.com.backend.repository;

import backend.com.backend.model.Donation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface DonationRepository extends JpaRepository<Donation, Long> {

    Optional<Donation> findByDonationCode(String donationCode);

    Page<Donation> findByDonationDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);

    Page<Donation> findByDonor_IdDonor(Long donorId, Pageable pageable);

    Page<Donation> findByStatus(Donation.DonationStatus status, Pageable pageable);
}
