package Banking.Application.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import Banking.Application.Entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
    boolean existsByAccountNumber(String accountNumber);
    User findByAccountNumber(String accountNumber);

}

