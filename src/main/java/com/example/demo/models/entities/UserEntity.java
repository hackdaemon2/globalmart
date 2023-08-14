package com.example.demo.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "users")
@NoArgsConstructor
public class UserEntity extends AbstractEntity {

    @Column(name = "first_name")
    @Size(message = "invalid length for firstName", min = 2, max = 255)
    private String firstName;

    @Column(name = "last_name")
    @Size(message = "invalid length for lastName", min = 2, max = 255)
    private String lastName;

    @Size(message = "invalid length for username", min = 2, max = 255)
    private String username;

    @Size(message = "minimum length for password is 14 characters", min = 14)
    private String password;

    private String email;

    @Pattern(regexp = "^(\\+)?\\d{11,15}$", message = "invalid phone passed")
    private String phone;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id", table = "users"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id", table = "roles"))
    private Set<RoleEntity> roles = new HashSet<>(1);

    @Override
    public String toString() {
        return new JSONObject()
                .put("firstName", firstName)
                .put("lastName", lastName)
                .put("username", username)
                .put("email", email).toString();
    }
}
