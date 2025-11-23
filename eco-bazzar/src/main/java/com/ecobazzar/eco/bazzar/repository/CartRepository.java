package com.ecobazzar.eco.bazzar.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ecobazzar.eco.bazzar.model.CartItem;

public interface CartRepository extends JpaRepository<CartItem , Long> {

    List<CartItem> findByUserId(Long id);
}
