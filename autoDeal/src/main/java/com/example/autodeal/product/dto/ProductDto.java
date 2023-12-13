package com.example.autodeal.product.dto;

import com.example.autodeal.product.enums.ProductType;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
@EqualsAndHashCode
public class ProductDto {
    private Integer id;
    private String name;
    private Integer price;
    private String carMake;
    private Integer mileage;
    private String origin;
    private ProductType type;
    private Long code;
    private String color;
    private Integer warranty;
    private Integer productionYear;
}
