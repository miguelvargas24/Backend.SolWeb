package backend.com.backend.repository;

import backend.com.backend.model.DistributionDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DistributionDetailRepository extends JpaRepository<DistributionDetail, Long> {

    List<DistributionDetail> findByDistribution_IdDistribution(Long distributionId);

    List<DistributionDetail> findByFoodItem_IdFoodItem(Long foodItemId);
}
