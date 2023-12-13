package com.example.autodeal.product.service;

import com.example.autodeal.product.enums.ProductType;
import com.example.autodeal.product.model.ProductModel;
import com.example.autodeal.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;


    public ProductModel findProductById(Integer id) {
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Could not find product by id"));
    }

    public List<ProductModel> findAllProducts() {
        return productRepository.findAll();
    }


    public ProductModel addProduct(ProductModel product) {
        return productRepository.save(product);
    }


    public List<ProductModel> findProductByColor(String color) {
        return productRepository.findByColor(color);
    }

    public List<ProductModel> findProductByCarMake(String carMake) {
        return productRepository.findByCarMake(carMake);
    }


    public List<ProductModel> findProductByProductionYear(Integer productionYear) {
        return productRepository.findByProductionYear(productionYear);
    }

    public List<ProductModel> findProductByType(ProductType type) {
        return productRepository.findByType(type);
    }

    public void deleteProduct(Integer productId) {
        productRepository.deleteById(productId);
    }

    public ProductModel updateProduct(ProductModel product) {
        return productRepository.save(product);
    }
}
