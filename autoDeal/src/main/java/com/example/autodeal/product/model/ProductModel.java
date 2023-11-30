package com.example.autodeal.product.model;

import jakarta.persistence.*;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
@Data
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
    private String carMake; //czyli marka samochodu :)

    @Column
    private String type; //czyli VAN/Combi/Sedan/SUV/Convertible

    @Column
    private Long code;

    @Column
    private String color;

    @Column
    private Integer warranty;

    @Column
    private Integer productionYear;
}
