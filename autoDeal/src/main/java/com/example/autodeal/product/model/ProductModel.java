package com.example.autodeal.product.model;

import com.example.autodeal.product.enums.ProductType;
import jakarta.persistence.*;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name= "product")
public class ProductModel {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "Name cannot be null")
    @Pattern(regexp = "^[a-zA-Z\\s]+", message = "Product name should only contain letters and spaces")
    @Column
    private String name;

    @Column
    private Integer price;

    @Column
    private String carMake;

    @Column
    private Integer mileage;

    @Column
    private String origin;

    @Column
    @Enumerated(EnumType.STRING)
    private ProductType type; //czyli VAN/Combi/Sedan/SUV/Convertible

    @Column
    private Long code;

    @Column
    private String color;

    @Column
    private Integer warranty;

    @Column
    private Integer productionYear;
}
