package com.ecobazzar.eco.bazzar.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecobazzar.eco.bazzar.model.Product;
import com.ecobazzar.eco.bazzar.model.User;
import com.ecobazzar.eco.bazzar.repository.UserRepository;
import com.ecobazzar.eco.bazzar.service.ProductService;

@RestController
@RequestMapping("api/products")
public class ProductController {


    private final ProductService productService;
    private final UserRepository userRepository;

    public ProductController(ProductService productService, UserRepository userRepository) {
        this.productService = productService;
        this.userRepository = userRepository;
    }

    @PreAuthorize("hasAnyRole('SELLER','ADMIN')")
    @PostMapping
    public Product addProduct(@RequestBody Product product) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User seller = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Seller not found"));
        product.setSellerId(seller.getId());
        return productService.createProduct(product);
    }

    @GetMapping
    public List<Product> listAllProducts() {
        return productService.getAllProducts();
    }

    @PreAuthorize("hasAnyRole('SELLER','ADMIN')")
    @GetMapping("/seller")
    public List<Product> listSellerProducts() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User seller = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Seller not found"));
        return productService.getProductsBySellerId(seller.getId());
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @PreAuthorize("hasAnyRole('SELLER','ADMIN')")
    @PutMapping("/{id}")
    public Product updateProductDetails(@PathVariable Long id, @RequestBody Product product) {
        return productService.updateProductDetails(id, product);
    }

    @PreAuthorize("hasAnyRole('SELLER','ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteProductDetails(@PathVariable Long id) {
        productService.deleteProductDetails(id);
    }
}
