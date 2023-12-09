package com.example.autodeal.util;

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

    }



}
