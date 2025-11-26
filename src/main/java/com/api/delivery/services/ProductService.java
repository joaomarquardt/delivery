package com.api.delivery.services;

import com.api.delivery.domain.Product;
import com.api.delivery.repositories.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product findProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + id));
    }

    public Product createProduct(Product product) {
        Product createdProduct = productRepository.save(product);
        return createdProduct;
    }

    public Product updateProduct(Long id, Product product) {
        Product createdProduct = productRepository.save(product);
        return createdProduct;
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
