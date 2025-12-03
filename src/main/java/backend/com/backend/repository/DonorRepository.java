package backend.com.backend.repository;

import backend.com.backend.model.Donor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DonorRepository extends JpaRepository<Donor, Long> {

    Optional<Donor> findByRuc(String ruc);

    Page<Donor> findByActiveTrue(Pageable pageable);

    Page<Donor> findByBusinessNameContainingIgnoreCaseAndActiveTrue(String businessName, Pageable pageable);
}
