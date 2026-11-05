package com.kelvin.spring_auth.entities.dtos;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class RegisterUserDto {
    @Column(nullable = false)
    @NotBlank(message = "Name is required")
    private String name;
    @Column(nullable = false)
    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address!!")
    private String email;
    @Column(nullable = false)
    @NotNull(message = "Password cannot be null")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&-+=()])(?=\\S+$).{8,72}$",
            message = "Password must be at least 8 characters long, contain at least one digit, one uppercase, one lowercase, and one special character."
    )
    private String password;

    public RegisterUserDto(String email, String name,String password){
        this.name=name;
        this.email=email;
        this.password=password;

    }

    public String getName(){
        return this.name;
    }

    public String getEmail(){
        return this.email;
    }

    public String getPassword(){
        return this.password;
    }
}
