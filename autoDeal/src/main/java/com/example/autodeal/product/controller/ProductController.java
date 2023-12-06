package com.example.autodeal.product.controller;

import com.example.autodeal.order.model.OrderModel;
import com.example.autodeal.product.dto.ProductDto;
import com.example.autodeal.product.enums.ProductType;
import com.example.autodeal.product.mapper.ProductMapper;
import com.example.autodeal.product.model.ProductModel;
import com.example.autodeal.product.service.ProductService;
import com.example.autodeal.user.model.UserModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

//getting product by id
    @GetMapping("/id/{productId}")
    public ResponseEntity<ProductDto> findProductById(@PathVariable("productId")Integer id){
        ProductModel productModel = productService.findProductById(id);
        return ResponseEntity.ok(ProductMapper.mapToProductDto(productModel));
    }

    @GetMapping("/color/{color}")
    public ResponseEntity<List<ProductModel>> findProductByColor(@PathVariable("color")String color){

        return ResponseEntity.ok(productService.findProductByColor(color));
    }

    @GetMapping("/carmake/{carMake}")
    public ResponseEntity<List<ProductModel>> findProductByCarMake(@PathVariable("carMake")String carMake){

        return ResponseEntity.ok(productService.findProductByCarMake(carMake));
    }

    @GetMapping("/year/{productionYear}")
    public ResponseEntity<List<ProductModel>> findProductByProductionYear(@PathVariable("productionYear")Integer productionYear, ProductModel getProduct){

        return ResponseEntity.ok(productService.findProductByProductionYear(productionYear));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<ProductModel>> findProductByType(@PathVariable("type")ProductType type){

        return ResponseEntity.ok(productService.findProductByType(type));
    }

    //Metody dla admina

    //Dodawanie nowego produktu
    @PostMapping("/admin/addProduct")
    public ResponseEntity<ProductDto> addNewProduct(@RequestBody ProductDto product){
     ProductModel productModel = ProductMapper.mapToProductModel(product);
       productModel = productService.addProduct(productModel);
        return ResponseEntity.ok(ProductMapper.mapToProductDto(productModel));
    }

    //Zmiana produktu(aktualizacja)
    @PutMapping("/admin/{productId}")

    public ResponseEntity<ProductModel> updateOrder(@PathVariable Integer productId, @RequestBody ProductModel product){
        product.setId(productId);
        return ResponseEntity.ok(productService.updateProduct(product));
    }

    @DeleteMapping("/admin/{productId}")
    // Usuwa produkt
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.ok().build();
    }

    //getting all products
    @GetMapping("/admin/products")
    public ResponseEntity<List<ProductModel>> showProducts(){

        return ResponseEntity.ok(productService.findAllProducts());
    }

}
