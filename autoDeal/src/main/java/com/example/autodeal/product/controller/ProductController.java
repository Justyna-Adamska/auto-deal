package com.example.autodeal.product.controller;

import com.example.autodeal.product.ProductMapper;
import com.example.autodeal.product.dto.ProductDto;
import com.example.autodeal.product.enums.ProductType;
import com.example.autodeal.product.model.ProductModel;
import com.example.autodeal.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    //getting product by id
    @GetMapping("/id/{productId}")
    public ResponseEntity<ProductDto> findProductById(@PathVariable("productId") Integer id) {
        ProductModel productModel = productService.findProductById(id);
        return ResponseEntity.ok(ProductMapper.mapToProductDto(productModel));
    }

    @GetMapping("/color/{color}")
    public ResponseEntity<List<ProductDto>> findProductByColor(@PathVariable("color") String color) {
        List<ProductModel> productModels = productService.findProductByColor(color);
        return ResponseEntity.ok(ProductMapper.mapToProductDtoList(productModels));
    }

    @GetMapping("/carmake/{carMake}")
    public ResponseEntity<List<ProductDto>> findProductByCarMake(@PathVariable("carMake") String carMake) {
        List<ProductModel> productModels = productService.findProductByCarMake(carMake);
        return ResponseEntity.ok(ProductMapper.mapToProductDtoList(productModels));
    }

    @GetMapping("/year/{productionYear}")
    public ResponseEntity<List<ProductDto>> findProductByProductionYear(@PathVariable("productionYear") Integer productionYear, ProductModel getProduct) {
        List<ProductModel> productModels = productService.findProductByProductionYear(productionYear);
        return ResponseEntity.ok(ProductMapper.mapToProductDtoList(productModels));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<ProductDto>> findProductByType(@PathVariable("type") ProductType type) {
        List<ProductModel> productModels = productService.findProductByType(type);
        return ResponseEntity.ok(ProductMapper.mapToProductDtoList(productModels));
    }

    //Metody dla admina

    //Dodawanie nowego produktu

    @PostMapping("/admin/addProduct")
    public ResponseEntity<ProductDto> addNewProduct(@RequestBody ProductDto product) {
        ProductModel productModel = ProductMapper.mapToProductModel(product);
        productModel = productService.addProduct(productModel);
        return ResponseEntity.ok(ProductMapper.mapToProductDto(productModel));
    }

    //Zmiana produktu(aktualizacja)
    @PutMapping("/admin/{productId}")

    public ResponseEntity<ProductDto> updateOrder(@PathVariable("productId") Integer productId, @RequestBody ProductDto product) {
        ProductModel productModel = ProductMapper.mapToProductModel(product);
        productModel = productService.updateProduct(productModel);
        return ResponseEntity.ok(ProductMapper.mapToProductDto(productModel));
    }

    @DeleteMapping("/admin/{productId}")
    // Usuwa produkt
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.ok().build();
    }

    //getting all products
    @GetMapping("/showProducts")
    // public ResponseEntity<List<ProductDto>> showProducts() {
    public String showProducts(Model model) {
        List<ProductModel> productModels = productService.findAllProducts();
        model.addAttribute("productModel", productModels);
        // return ResponseEntity.ok(ProductMapper.mapToProductDtoList(productModels));
        return "admin/showProducts";

    }

}
