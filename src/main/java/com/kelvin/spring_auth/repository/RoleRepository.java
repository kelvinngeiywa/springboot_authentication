package com.kelvin.spring_auth.repository;

import com.kelvin.spring_auth.entities.Role;
import com.kelvin.spring_auth.entities.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


public interface RoleRepository extends JpaRepository<Role, UUID> {
    Optional<Role> findRoleByName(RoleEnum rolename);
}
