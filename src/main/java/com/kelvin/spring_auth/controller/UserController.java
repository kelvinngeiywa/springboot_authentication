package com.kelvin.spring_auth.controller;

import com.kelvin.spring_auth.entities.User;
import com.kelvin.spring_auth.entities.dtos.LoginRequestDto;
import com.kelvin.spring_auth.entities.dtos.LoginResponseDto;
import com.kelvin.spring_auth.entities.dtos.RegisterUserDto;
import com.kelvin.spring_auth.services.JwtServices;
import com.kelvin.spring_auth.services.UserServices;
import io.jsonwebtoken.Jwt;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.apache.juli.logging.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("users")
public class UserController {
    private final UserServices userServices;
    private final JwtServices jwtServices;

    public UserController(UserServices userServices, JwtServices jwtServices){
        this.userServices=userServices;
        this.jwtServices=jwtServices;

    }

    @PostMapping("/register")
    public ResponseEntity<User> addNewUser(@Valid @RequestBody RegisterUserDto newUserDto){
        User newUser= userServices.createNewUser(newUserDto);
        return ResponseEntity.ok(newUser);

    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> loginUser(@RequestBody LoginRequestDto loginRequestDto){
        User loggedUser= userServices.Login(loginRequestDto);

        String jwtToken=jwtServices.generateToken(loggedUser); //casting
        LoginResponseDto loginResponseDto=new LoginResponseDto();
        loginResponseDto.setToken(jwtToken);
        loginResponseDto.setExpiresIn(jwtServices.getExpirationTime());

        return ResponseEntity.ok(loginResponseDto);
    }



    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<List<RegisterUserDto>> getAllUsers(){
        List<RegisterUserDto> users= userServices.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<RegisterUserDto> getUserById(@PathVariable UUID id){
        RegisterUserDto findUser= userServices.getUserById(id);
        return ResponseEntity.ok(findUser);
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<User> authenticatedUser(){

        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication + " / " + (authentication == null ? null : authentication.getPrincipal()));
        User currentuser= (User)authentication.getPrincipal();
        System.out.println(currentuser.getEmail());

        return ResponseEntity.ok(currentuser);

    }
}
