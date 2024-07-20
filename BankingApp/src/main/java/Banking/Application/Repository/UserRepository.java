package Banking.Application.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import Banking.Application.Entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);
    boolean existsByAccountNumber(String accountNumber);
    User findByAccountNumber(String accountNumber);
}

