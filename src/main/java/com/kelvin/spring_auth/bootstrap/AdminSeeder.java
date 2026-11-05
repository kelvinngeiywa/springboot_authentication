package com.kelvin.spring_auth.bootstrap;

import com.kelvin.spring_auth.entities.Role;
import com.kelvin.spring_auth.entities.RoleEnum;
import com.kelvin.spring_auth.entities.User;
import com.kelvin.spring_auth.entities.dtos.RegisterUserDto;
import com.kelvin.spring_auth.repository.RoleRepository;
import com.kelvin.spring_auth.repository.UserRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AdminSeeder implements ApplicationListener<ContextRefreshedEvent> {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public AdminSeeder(RoleRepository roleRepository,UserRepository userRepository,
                       PasswordEncoder passwordEncoder){
        this.userRepository=userRepository;
        this.roleRepository=roleRepository;
        this.passwordEncoder=passwordEncoder;

    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent){
        this.createSuperAdministrator();

    }

    private void createSuperAdministrator(){
        RegisterUserDto userDto=new RegisterUserDto();
        userDto.setName("Super Admin");
        userDto.setEmail("super.admin@email.com");
        userDto.setPassword("Superadmin@123");

        Optional<Role> optionalRole= roleRepository.findRoleByName(RoleEnum.SUPER_ADMIN);
        Optional<User> optionalUser= userRepository.findByEmail(userDto.getEmail());

        if(optionalRole.isEmpty() || optionalUser.isPresent()){
            return;
        }

        var user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRole(optionalRole.get());

        userRepository.save(user);
    }
}
