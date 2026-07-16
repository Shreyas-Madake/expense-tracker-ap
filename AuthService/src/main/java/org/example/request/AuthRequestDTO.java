package org.example.request;
// request pakage is used for creating DTOs (Data Transfer Objects) that are used to transfer data between the client and the server. These DTOs are typically used in the controller layer to receive data from the client and pass it to the service layer for processing. In this case, AuthRequestDTO is a DTO that contains the username and password fields that are required for authentication.

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthRequestDTO {

    private String username;
    private String password;

    // Explicit getters for static analysis
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
