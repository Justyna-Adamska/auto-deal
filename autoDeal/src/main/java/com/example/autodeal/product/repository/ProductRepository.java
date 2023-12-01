package com.example.autodeal.product.repository;

import com.example.autodeal.product.model.ProductModel;
import com.example.autodeal.user.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductModel, Integer> {

    List<ProductModel> findByColor(String color);
    List<ProductModel> findByCarMake(String carMake);
    List<ProductModel> findByProductionYear(Integer productionYear);
    List<ProductModel> findByType(String Type);


}
