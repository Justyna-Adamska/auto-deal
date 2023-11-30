package com.example.autodeal.user.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Data
@Table(name = "customer")
public class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "First name cannot be null")
    @Pattern(regexp = "^[a-zA-Z\\s]+", message = "First name should only contain letters and spaces")
    @Column(name = "firstName", nullable = false)
    private String firstName;

    @NotNull(message = "Last name cannot be null")
    @Pattern(regexp = "^[a-zA-Z\\s]+", message = "Last name should only contain letters and spaces")
    @Column(name = "lastName", nullable = false)
    private String lastName;

    @NotNull(message = "Password cannot be null")
    @Column(nullable = false)
    private String password;

    @NotNull(message = "Email cannot be null")
    @Pattern(regexp = "^(.+)@(.+)$", message = "Invalid email format")
    @Column(nullable = false, unique = true)
    private String email;

    @NotNull(message = "Please Enter your Mobile Number")
    @Pattern(regexp = "[7896]{1}[0-9]{9}",message = "Input a valid mobile number")
    @Column(nullable = false, unique = true)
    private String phone;

    @Column(name = "lastLoginDate")
    private LocalDateTime lastLoginDate;

    @Column(name = "enabled")
    private Boolean enabled;

    public UserModel() {
        super();
        this.enabled=false;
    }

    @Column(name = "resetToken")
    private String resetToken;

    @ManyToMany
    @JoinTable (name="user_x_roles")
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<UserRole> roles;

}
