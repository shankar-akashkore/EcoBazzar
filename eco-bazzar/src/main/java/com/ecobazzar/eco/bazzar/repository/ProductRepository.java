package com.ecobazzar.eco.bazzar.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ecobazzar.eco.bazzar.model.Product;

public interface ProductRepository extends JpaRepository<Product , Long> {

    List<Product> findByEcoCertifiedTrue();

    List<Product> findByEcoCertifiedTrueOrderByCarbonImpactAsc();

    Optional<Product> findFirstByEcoCertifiedTrueAndNameContainingIgnoreCase(String namePart);

    Optional<Product> findFirstByEcoCertifiedFalseAndNameContainingIgnoreCase(String keyword);

    List<Product> findByEcoRequestedTrue();

    List<Product> findBySeller_Id(Long sellerId);

    List<Product> findByEcoCertifiedTrueAndNameContainingIgnoreCase(String name);

    List<Product> findByEcoCertifiedFalse();
}

