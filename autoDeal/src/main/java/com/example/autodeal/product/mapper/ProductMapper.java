package com.example.autodeal.product.mapper;

import com.example.autodeal.product.dto.ProductDto;
import com.example.autodeal.product.model.ProductModel;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class ProductMapper {

    public static ProductDto mapToProductDto(ProductModel productModel) {
        return ProductDto.builder()
                .id(Integer.valueOf(productModel.getId()))
                .name(productModel.getName())
                .price(productModel.getPrice())
                .carMake(productModel.getCarMake())
                .mileage(productModel.getMileage())
                .origin(productModel.getOrigin())
                .type(productModel.getType())
                .code(productModel.getCode())
                .color(productModel.getColor())
                .warranty(productModel.getWarranty())
                .productionYear(productModel.getProductionYear())
                .build();
    }


    public static List<ProductDto> mapToProductDtoList(List<ProductModel> productModels) {
        List<ProductDto> productList = new ArrayList<>();
        for (ProductModel productModel : productModels) {
            productList.add(mapToProductDto(productModel));
        }
        return productList;

    }
}