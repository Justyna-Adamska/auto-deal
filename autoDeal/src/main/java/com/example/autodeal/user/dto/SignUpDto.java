package com.example.autodeal.user.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class SignUpDto {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String password;
}