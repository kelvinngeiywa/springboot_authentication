package com.kelvin.spring_auth.bootstrap;

import com.kelvin.spring_auth.entities.Role;
import com.kelvin.spring_auth.entities.RoleEnum;
import com.kelvin.spring_auth.repository.RoleRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

@Component
public class RoleSeeder implements ApplicationListener<ContextRefreshedEvent> {

    private final RoleRepository roleRepository;

    public RoleSeeder(RoleRepository roleRepository){
        this.roleRepository=roleRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent){
        this.loadRoles();

    }

    private void loadRoles(){
        RoleEnum[] roleNames= new RoleEnum[] {RoleEnum.USER,RoleEnum.ADMIN,RoleEnum.SUPER_ADMIN};
        Map<RoleEnum,String> roleEnumStringMap=Map.of(
                RoleEnum.USER,"Default user role",
                RoleEnum.ADMIN,"Administrator role",
                RoleEnum.SUPER_ADMIN,"Super administrator role"
        );

        Arrays.stream(roleNames).forEach((roleName)->{
            Optional<Role> optionalRole=roleRepository.findRoleByName(roleName);

            optionalRole.ifPresentOrElse(System.out::println,()->{
                Role roleToCreate = new Role();

                roleToCreate.setName(roleName);
                roleToCreate.setDescription(roleEnumStringMap.get(roleName));

                roleRepository.save(roleToCreate);
            });


        });
    }
}
