package backend.com.backend.repository;

import backend.com.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // JpaRepository ya extiende PagingAndSortingRepository,
    // por lo que tenemos acceso a findAll(Pageable pageable) automáticamente.

    java.util.Optional<User> findByEmail(String email);
}
