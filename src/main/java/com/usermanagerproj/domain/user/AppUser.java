package com.usermanagerproj.domain.user;

import com.usermanagerproj.domain.role.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "user_account",
        uniqueConstraints = {@UniqueConstraint(name = "unique_user_email",columnNames = "email"),
                             @UniqueConstraint(name = "unique_user_username",columnNames = "username")}

)

public class AppUser {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;
    @Column(length = 50)
    private String firstName;
    @Column(length = 50)
    private String lastName;
    @Column(
            nullable = false,
            length = 50
    )
    private String username;
    @Column(
            nullable = false,
            length = 50
    )
    private String email;
    @Column(
            nullable = false
    )
    private String password;
    @Column
    private Integer age;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @Column(name = "blocked")
    private Boolean isBlocked;
    @Column(name = "enabled")
    private Boolean isEnabled;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public void setRoles(Set<Role> roleByName) {
        this.roles = roleByName;
    }
}
