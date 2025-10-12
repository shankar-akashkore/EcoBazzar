package com.ecobazzar.eco.bazzar.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ecobazzar.eco.bazzar.model.Product;

public interface ProductRepository extends JpaRepository<Product , Long> {

	List<Product> findByEcoCertifiedTrue();
	List<Product> findByEcoCertifiedTrueOrderByCarbonImpactAsc();
}
