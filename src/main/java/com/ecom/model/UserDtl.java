package com.ecom.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class UserDtl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @NotBlank
    private String name;

    @Size(min = 10, max = 10 ,message="Phone number must contain 10 digit")
    private String mobileNumber;

    @Email
    @NotBlank
    private String email;

    private String address;

    private String city;

    @NotNull
    @NotBlank
    private String state;


    @Digits(integer = 6, fraction = 0)
    private Integer pincode;

    @NotBlank
    @Size(min = 4, max = Integer.MAX_VALUE)
    private String password;
    
    @NotNull
    @Size(min = 4, max = Integer.MAX_VALUE)
    private String confirmPassword;

    private String profileImage;

    private String role;

    private Boolean isEnabled;

    private Boolean accountNonLocked=true;

    private Integer failedAttempt=0;

    private Date lockTime;

    private String token;



}
