package com.kelvin.spring_auth.repository;


import com.kelvin.spring_auth.entities.User;
import com.kelvin.spring_auth.entities.dtos.LoginRequestDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    User findUserByEmail(String email);
    boolean existsByEmail(String email);
//    User loginUser(LoginRequestDto loginRequestDto);

//    String getUserByEmail(String email);
}
