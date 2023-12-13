package com.example.autodeal.product.repository;

import com.example.autodeal.product.enums.ProductType;
import com.example.autodeal.product.model.ProductModel;

import static org.assertj.core.api.Assertions.assertThat;

import lombok.Data;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
@ExtendWith(MockitoExtension.class)
@DataJpaTest
class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;


    @BeforeEach
    void setUp() {
        ProductModel productModel = new ProductModel();
        productModel.setName("Test");
        productModel.setPrice(1);
        productModel.setCarMake("BMW");
        productModel.setMileage(10000);
        productModel.setOrigin("Polska");
        productModel.setType(ProductType.COMBI);
        productModel.setColor("white");
        productModel.setWarranty(1);
        productModel.setProductionYear(1989);
        productRepository.save(productModel);
    }

    @Test
    void whenFindByColorThenShouldReturnProduct() {
        //When
        List<ProductModel> foundCar = productRepository.findByColor("white");

        //Then
        assertThat(foundCar).isNotEmpty();
        assertThat(foundCar).size().isEqualTo(1);
        assertThat(foundCar.get(0).getColor().equals("white"));
    }

    @Test
    void WhenFindByCarMakeThenShouldReturnProduct() {
        //When
        List<ProductModel> foundCar = productRepository.findByCarMake("BMW");

        //Then
        assertThat(foundCar).isNotEmpty();
        assertThat(foundCar).size().isEqualTo(1);
        assertThat(foundCar.get(0).getCarMake().equals("BMW"));
    }

    @Test
    void WhenFindByProductionYearThenShouldReturnProduct() {
        //When
        List<ProductModel> foundCar = productRepository.findByProductionYear(1989);

        //Then
        assertThat(foundCar).isNotEmpty();
        assertThat(foundCar).size().isEqualTo(1);
        assertThat(foundCar.get(0).getProductionYear().equals(1989));
    }

    @Test
    void WhenFindByTypeThenShouldReturnProduct() {
        //When
        List<ProductModel> foundCar = productRepository.findByType(ProductType.COMBI);

        //Then
        assertThat(foundCar).isNotEmpty();
        assertThat(foundCar).size().isEqualTo(1);
        assertThat(foundCar.get(0).getType().equals(ProductType.COMBI));
    }
}