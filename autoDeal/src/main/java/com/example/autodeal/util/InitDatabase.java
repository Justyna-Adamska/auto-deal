package com.example.autodeal.util;

import com.example.autodeal.product.enums.ProductType;
import com.example.autodeal.product.model.ProductModel;
import com.example.autodeal.product.repository.ProductRepository;
import com.example.autodeal.user.model.UserModel;
import com.example.autodeal.user.model.UserRole;
import com.example.autodeal.user.repository.UserRepository;
import com.example.autodeal.user.repository.UserRoleRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.management.relation.Role;
import java.util.Set;

@RequiredArgsConstructor
@Component
public class InitDatabase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRoleRepository userRoleRepository;
    private final ProductRepository productRepository;
    @PostConstruct
    public void  init(){

        UserRole userRole = new UserRole();
        userRole.setName("ROLE_ADMIN");
        UserRole savedAdmin = userRoleRepository.save(userRole);

        UserRole userRole2 = new UserRole();
        userRole2.setName("ROLE_USER");
        UserRole savedAdmin2 = userRoleRepository.save(userRole2);


        UserModel userModel = new UserModel();

        userModel.setFirstName("Marian");
        userModel.setLastName("Kowalski");
        userModel.setPassword(passwordEncoder.encode("abc"));
        userModel.setEmail("jan.kowalski@gmail.com");
        userModel.setPhone("501-151-658");
        userModel.setEnabled(true);
        userModel.setRoles(Set.of(savedAdmin));
        userRepository.save(userModel);



        UserModel userModel2 = new UserModel();

        userModel2.setFirstName("Jan");
        userModel2.setLastName("Kot");
        userModel2.setPassword(passwordEncoder.encode("asd"));
        userModel2.setEmail("jan.kot@gmail.com");
        userModel2.setPhone("501-965-123");
       userModel2.setEnabled(true);
        userModel2.setRoles(Set.of(savedAdmin2));//savedUser
        userRepository.save(userModel2);

        ProductModel productModel1 = new ProductModel();

        productModel1.setName("Dacia prawie niebita");
        productModel1.setPrice(10000);
        productModel1.setCarMake("Dacia");
        productModel1.setMileage(200000);
        productModel1.setOrigin("Maroko");
        productModel1.setType(ProductType.VAN);
        productModel1.setCode(1234567L);
        productModel1.setColor("white");
        productModel1.setWarranty(1);
        productModel1.setProductionYear(2019);
        productRepository.save(productModel1);

        ProductModel productModel2 = new ProductModel();

        productModel2.setName("Passat jak nowy");
        productModel2.setPrice(5000);
        productModel2.setCarMake("Volkswagen");
        productModel2.setMileage(300000);
        productModel2.setOrigin("Niemcy");
        productModel2.setType(ProductType.SEDAN);
        productModel2.setCode(7654321L);
        productModel2.setColor("black");
        productModel2.setWarranty(0);
        productModel2.setProductionYear(2016);
        productRepository.save(productModel2);

        ProductModel productModel3 = new ProductModel();

        productModel3.setName("Renault prosto z Paryża");
        productModel3.setPrice(5500);
        productModel3.setCarMake("Renault");
        productModel3.setMileage(30000);
        productModel3.setOrigin("Francja");
        productModel3.setType(ProductType.HATCHBACK);
        productModel3.setCode(2222222L);
        productModel3.setColor("red");
        productModel3.setWarranty(2);
        productModel3.setProductionYear(2006);
        productRepository.save(productModel3);

    }

//INSERT INTO product (name, price, car_make, mileage, origin, type, code, color,
// warranty,production_year) VALUES ('Dacia prawie niebita', '10000','Dacia','200000',
// 'Maroko','VAN', '1234567', 'biały', '1','2019');

}
