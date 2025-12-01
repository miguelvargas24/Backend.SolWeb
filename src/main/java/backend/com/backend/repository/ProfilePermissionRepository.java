package backend.com.backend.repository;

import backend.com.backend.model.ProfilePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfilePermissionRepository extends JpaRepository<ProfilePermission, Long> {
    List<ProfilePermission> findByProfile_IdProfile(Long profileId);
}
