package org.example.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "roles")
public class UserRole {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)// to auto genegrate in db the nos(eg1,2,3)
    @Column(name="role_id")
    private Long roleId;
    private String name;

    // Explicit getter for static analysis/IDE
    public String getName() {
        return name;
    }

}
