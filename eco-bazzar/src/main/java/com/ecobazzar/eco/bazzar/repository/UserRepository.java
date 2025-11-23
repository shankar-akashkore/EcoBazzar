package com.ecobazzar.eco.bazzar.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ecobazzar.eco.bazzar.model.User;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long>{

    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    List<User> findBySellerRequestPendingTrue();
}
