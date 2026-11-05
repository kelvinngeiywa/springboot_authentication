package com.kelvin.spring_auth.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false)
    private UUID id;
    @Column(unique = true,nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleEnum name;
    @Column(nullable = false)
    private String description;
    @CreationTimestamp
    @Column(updatable = false,name = "created_at")
    private LocalDate createdAt;
    @UpdateTimestamp
    @Column
    private LocalDate updated_at;

    public Role(){

    }

    public Role(UUID id,RoleEnum name,String description,LocalDate createdAt,LocalDate updated_at){
        this.id=id;
        this.name=name;
        this.description=description;
        this.createdAt=createdAt;
        this.updated_at=updated_at;

    }

    public UUID getId(){
        return this.id;
    }

    public RoleEnum getName(){
        return this.name;
    }

    public void setName(RoleEnum rolename){
        this.name=rolename;
    }

    public String getDescription(){
        return this.description;
    }

    public void setDescription(String description){
        this.description=description;
    }

    public LocalDate getCreatedAt(){
        return this.createdAt;
    }

    public LocalDate getUpdated_at(){
        return this.updated_at;
    }

}
