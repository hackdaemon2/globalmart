package com.globalmart.app.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "roles")
@NoArgsConstructor
public class RoleEntity extends AbstractEntity {

    @Column(name = "name")
    @Size(message = "invalid length for name", min = 2, max = 255)
    private String name;

}
