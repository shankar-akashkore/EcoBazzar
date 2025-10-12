package com.ecobazzar.eco.bazzar.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecobazzar.eco.bazzar.model.Product;
import com.ecobazzar.eco.bazzar.service.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController {

	public final ProductService productService;
	
	public ProductController(ProductService productService) {
		this.productService = productService;
	}
	
	@PostMapping
	public Product addProduct(@RequestBody Product product) {
		return productService.createProduct(product);
	}
	
	@GetMapping
	public List<Product> listAllProduct(){
		return productService.getAllProducts();
	}
	
	@PutMapping("/{id}")
	public Product updateProductDetails(@PathVariable Long id, @RequestBody Product product) {
		return productService.updateProductDetails(id, product);
	}
	
	@DeleteMapping("/{id}")
	public void deleteProductDetails(@PathVariable Long id) {
		productService.deleteProductDetails(id);
	}
	
	@GetMapping("/eco")
	public List<Product> getEcoCertified(){
		return productService.getEcoCertifiedProducts();
	}
	
	@GetMapping("/eco/sorted")
	public List<Product> getEcoCertifiedSorted(){
		return productService.getEcoCertifiedSortedByCarbonImpact();
	}
	
}
