package com.example.autodeal.product.mapper;

import com.example.autodeal.product.dto.ProductDto;
import com.example.autodeal.product.mapper.ProductMapper;
import com.example.autodeal.product.model.ProductModel;
import com.example.autodeal.product.enums.ProductType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProductMapperTest {

    @Test
    void mapToProductDto_WithValidProductModel_ShouldReturnCorrectProductDto() {
        ProductModel productModel = ProductModel.builder()
                .id(1)
                .name("Test Product")
                .price(100)
                .carMake("Test Make")
                .mileage(5000)
                .origin("Test Origin")
                .type(ProductType.SEDAN)
                .code(12345L)
                .color("Red")
                .warranty(2)
                .productionYear(2020)
                .build();

        ProductDto productDto = ProductMapper.mapToProductDto(productModel);

        assertNotNull(productDto);
        assertEquals(productModel.getId(), productDto.getId());
        assertEquals(productModel.getName(), productDto.getName());
        assertEquals(productModel.getPrice(), productDto.getPrice());
        assertEquals(productModel.getCarMake(), productDto.getCarMake());
        assertEquals(productModel.getMileage(), productDto.getMileage());
        assertEquals(productModel.getOrigin(), productDto.getOrigin());
        assertEquals(productModel.getType(), productDto.getType());
        assertEquals(productModel.getCode(), productDto.getCode());
        assertEquals(productModel.getColor(), productDto.getColor());
        assertEquals(productModel.getWarranty(), productDto.getWarranty());
        assertEquals(productModel.getProductionYear(), productDto.getProductionYear());
    }
}