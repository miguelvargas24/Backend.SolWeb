package backend.com.backend.repository;

import backend.com.backend.model.DonationDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DonationDetailRepository extends JpaRepository<DonationDetail, Long> {

    List<DonationDetail> findByDonation_IdDonation(Long donationId);

    List<DonationDetail> findByFoodItem_IdFoodItem(Long foodItemId);
}
