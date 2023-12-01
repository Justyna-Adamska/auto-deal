package com.example.autodeal.product.controller;

import com.example.autodeal.product.model.ProductModel;
import com.example.autodeal.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@Slf4j
@RequiredArgsConstructor
public class AdminProductController {

    private final ProductService productService;

    @PostMapping("/addProduct")
    public RedirectView addNewProduct(ProductModel product){
        productService.addProduct(product);

        return new RedirectView(("/products"));//po dodaniu uzytkownika przenosi nas na stronę ze wszystkimi produktami
    }
}
