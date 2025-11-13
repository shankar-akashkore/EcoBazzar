package com.ecobazzar.eco.bazzar.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ecobazzar.eco.bazzar.model.Product;
import com.ecobazzar.eco.bazzar.repository.ProductRepository;

@Service
public class ProductService {

	private final ProductRepository productRepository;
	
	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}
	
	public Product createProduct(Product product) {
		product.setEcoCertified(false);
		return productRepository.save(product);
	}
	
	public List<Product> getAllProducts(){
		return productRepository.findAll();
	}
	
	public Product updateProductDetails(Long id, Product updateProduct) {
		return productRepository.findById(id)
				.map(product->{
					product.setName(updateProduct.getName());
					product.setDetails(updateProduct.getDetails());
					product.setPrice(updateProduct.getPrice());
					product.setCarbonImpact(updateProduct.getCarbonImpact());
					product.setSellerId(updateProduct.getSellerId());
					return productRepository.save(product);
				})
				.orElseThrow(()-> new RuntimeException("Product not found"));
	}
	
	public void deleteProductDetails(Long id) {
		productRepository.deleteById(id);
	}
	
	public List<Product> getEcoCertifiedProducts(){
		return productRepository.findByEcoCertifiedTrue();
	}
	
	public List<Product> getEcoCertifiedSortedByCarbonImpact() {
		return productRepository.findByEcoCertifiedTrueOrderByCarbonImpactAsc();
	}
	
}
