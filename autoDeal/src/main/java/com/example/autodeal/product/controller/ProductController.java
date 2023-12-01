package com.example.autodeal.product.controller;

import com.example.autodeal.product.model.ProductModel;
import com.example.autodeal.product.service.ProductService;
import com.example.autodeal.user.model.UserModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/products")
    public String showProducts(Model model){
        List<ProductModel> productList = productService.findAllProducts();
        model.addAttribute("productModel", productList);
        return "products";
    }

    @GetMapping("/products/{id}")
    public String findProductById(@PathVariable("id")Integer id, ProductModel getProduct){
        productService.findProductById(id);
        return "product/{id}";
    }

    @GetMapping("/products/{color}")
    public String findProductByColor(@PathVariable("color")String color, ProductModel getProduct){
        productService.findProductByColor(color);
        return "product/{color}";
    }

    @GetMapping("/products/{carMake}")
    public String findProductByCarMake(@PathVariable("carMake")String carMake, ProductModel getProduct){
        productService.findProductByCarMake(carMake);
        return "product/{carMake}";
    }

    @GetMapping("/products/{productionYear}")
    public String findProductByProductionYear(@PathVariable("productionYear")Integer productionYear, ProductModel getProduct){
        productService.findProductByProductionYear(productionYear);
        return "product/{productionYear}";
    }

    @GetMapping("/products/{type}")
    public String findProductByType(@PathVariable("type")String type, ProductModel getProduct){
        productService.findProductByType(type);
        return "product/{type}";
    }


}
