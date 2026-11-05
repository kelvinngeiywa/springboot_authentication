package com.kelvin.spring_auth.controller;

import com.kelvin.spring_auth.entities.User;
import com.kelvin.spring_auth.entities.dtos.RegisterUserDto;
import com.kelvin.spring_auth.services.UserServices;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admins")
public class AdminController {
    private final UserServices userServices;

    public AdminController(UserServices userServices){
        this.userServices=userServices;
    }

    @PostMapping
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<User> createAdministrator(@RequestBody RegisterUserDto registerUserDto){
        User createAdmin=userServices.createAdministrator(registerUserDto);

        return ResponseEntity.ok(createAdmin);

    }
}
