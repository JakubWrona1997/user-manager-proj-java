package com.usermanagerproj.domain.user;

import com.usermanagerproj.domain.role.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

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
    @ManyToMany(fetch = FetchType.EAGER,
    cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    @Fetch(FetchMode.SUBSELECT)
    private Set<Role> roles = new HashSet<>();

    public void setRoles(Set<Role> roleByName) {
        this.roles = roleByName;
    }

    public AppUser(String firstName, String lastName, String username, String email, String password, Integer age, LocalDateTime createdAt, LocalDateTime updatedAt, Boolean isBlocked, Boolean isEnabled, Set<Role> roles) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.age = age;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isBlocked = isBlocked;
        this.isEnabled = isEnabled;
        this.roles = roles;
    }
}
