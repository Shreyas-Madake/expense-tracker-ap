package org.example.response;
// response pakage is used for creating response DTOs (Data Transfer Objects) that are used to send data back to the client in a structured format. These DTOs typically contain fields that represent the data being sent, along with any necessary annotations for serialization and deserialization. In this case, JwtResponseDTO is likely used to encapsulate the response data related to JWT (JSON Web Token) authentication, including the access token and any additional token information.
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtResponseDTO {

    private String accessToken;
    private String token;

    // Explicit all-args constructor for static analysis / IDEs that don't process Lombok
 //   public JwtResponseDTO(String accessToken, String token) {
 //       this.accessToken = accessToken;
 //       this.token = token;
 //   }
}
