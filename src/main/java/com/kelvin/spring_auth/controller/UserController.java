package com.kelvin.spring_auth.controller;

import com.kelvin.spring_auth.entities.User;
import com.kelvin.spring_auth.entities.dtos.LoginRequestDto;
import com.kelvin.spring_auth.entities.dtos.RegisterUserDto;
import com.kelvin.spring_auth.services.UserServices;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.apache.juli.logging.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("users")
public class UserController {
    private final UserServices userServices;

    public UserController(UserServices userServices){
        this.userServices=userServices;

    }

    @PostMapping("register")
    public ResponseEntity<User> addNewUser(@Valid @RequestBody RegisterUserDto newUserDto){
        User newUser= userServices.createNewUser(newUserDto);
        return ResponseEntity.ok(newUser);

    }

    @PostMapping("/login")
    public ResponseEntity<User> loginUser(@RequestBody LoginRequestDto loginRequestDto){
        User loggedUser= userServices.Login(loginRequestDto);
        return ResponseEntity.ok(loggedUser);
    }



    @GetMapping
    public ResponseEntity<List<RegisterUserDto>> getAllUsers(){
        List<RegisterUserDto> users= userServices.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("{id}")
    public ResponseEntity<RegisterUserDto> getUserById(UUID id){
        RegisterUserDto findUser= userServices.getUserById(id);
        return ResponseEntity.ok(findUser);
    }
}
