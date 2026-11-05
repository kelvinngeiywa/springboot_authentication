package com.kelvin.spring_auth.entities.dtos;

public class LoginRequestDto {
    private String email;
    private String password;

    public String getEmail(){
        return this.email;
    }

    public String getPassword(){
        return this.password;
    }

}
