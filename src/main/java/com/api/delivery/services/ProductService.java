package com.api.delivery.services;

import com.api.delivery.domain.Product;
import com.api.delivery.dtos.requests.CreateProductRequest;
import com.api.delivery.dtos.requests.UpdateProductRequest;
import com.api.delivery.dtos.responses.ProductResponse;
import com.api.delivery.mappers.ProductMapper;
import com.api.delivery.repositories.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    public List<ProductResponse> findAll() {
        List<Product> products = productRepository.findAll();
        return productMapper.toProductResponseList(products);
    }

    public Product findProductEntityById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + id));
    }

    public ProductResponse findProductById(Long id) {
        Product product = findProductEntityById(id);
        return productMapper.toProductResponse(product);
    }

    public ProductResponse createProduct(CreateProductRequest request) {
        Product product = productMapper.toProductEntity(request);
        Product createdProduct = productRepository.save(product);
        return productMapper.toProductResponse(createdProduct);
    }

    public ProductResponse updateProduct(Long id, UpdateProductRequest request) {
        Product product = findProductEntityById(id);
        productMapper.updateProductFromRequest(request, product);
        Product createdProduct = productRepository.save(product);
        return productMapper.toProductResponse(createdProduct);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
