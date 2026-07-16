package org.example.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@ToString
@Table(name = "users")  
public class UserInfo {

    @Id
    @Column(name = "user_id")
    private String userId;

    private String username;

    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )

    private Set<UserRole> roles = new HashSet<>();// we store the roles of user in set because we dont want to have duplicate role for a user

    public UserInfo() {
    }

    public UserInfo(String userId, String username, String password, Set<UserRole> roles) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.roles = roles != null ? roles : new HashSet<>();
    }

    // Explicit getters added to satisfy static analysis / IDE that may not process Lombok annotations
    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Set<UserRole> getRoles() {
        return roles;
    }


//we also use eager fetching to load the roles of user when we load the user from database
    //we aso use join table to create a many to many relationship between user and role because one user can have multiple roles and one role can be assigned to multiple users
    //we also use UserRole class to represent the role of user and we will create a separate table for roles in database and we will use the role id to link the user and role tables together
   // we created another table called users_roles to store the relationship between user and role because we have a many to many relationship between user and role and we need to store the relationship in a separate table in database
}
