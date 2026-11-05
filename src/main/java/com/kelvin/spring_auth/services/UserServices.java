package com.kelvin.spring_auth.services;

import com.kelvin.spring_auth.entities.User;
import com.kelvin.spring_auth.entities.dtos.LoginRequestDto;
import com.kelvin.spring_auth.entities.dtos.RegisterUserDto;
import com.kelvin.spring_auth.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServices {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;


    public UserServices(UserRepository userRepository,PasswordEncoder passwordEncoder,
    AuthenticationManager authenticationManager){
        this.userRepository=userRepository;
        this.passwordEncoder=passwordEncoder;
        this.authenticationManager=authenticationManager;


    }

    public User createNewUser(RegisterUserDto newUser){
        if(userRepository.existsByEmail(newUser.getEmail())){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Email exists");
        }
        User user= new User();
        user.setName(newUser.getName());
        user.setEmail(newUser.getEmail());
        user.setPassword(passwordEncoder.encode(newUser.getPassword()));

        return userRepository.save(user);
    }

    public User Login(LoginRequestDto loginRequestDto){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.getEmail(),
                        loginRequestDto.getPassword()
                )

        );
        return userRepository.findByEmail(loginRequestDto.getEmail()).orElseThrow();
    }

    public List<RegisterUserDto> getAllUsers(){
        return userRepository.findAll().stream()
                .map(u->new RegisterUserDto(u.getName(),u.getEmail(),u.getPassword()))
                .toList();
    }

    public RegisterUserDto getUserById(UUID id){
        return userRepository.findById(id).map(u->
                new RegisterUserDto(u.getName(),u.getEmail(),u.getPassword()))
                .orElseThrow(()->new RuntimeException("User with id "+id+" not found"))
                ;
    }

    public User getUserByEmail(LoginRequestDto loginRequestDto){
        User user= userRepository.findUserByEmail(loginRequestDto.getEmail());
        if (user==null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        if(!user.getPassword().equals(loginRequestDto.getPassword())){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid password");
        }
        System.out.println(user.getEmail());
        return user;
    }

//    public User findByEmail(String email){
//        return userRepository.findByEmailExists(email);
//
//    }
}
