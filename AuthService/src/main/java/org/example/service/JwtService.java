package org.example.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    public static final String SECRET = "357638792F423F4428472B4B6250655368566D597133743677397A2443264629";

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);// here we are extracting the subject from the token
        // subject is nothing but the username
        // claim is nothing but the information stored in the token
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {// function is a functional interface that takes an input and returns an output and claimresolver is the function that we are passing
        final Claims claims = extractAllClaims(token);// here we are extracting all claims from the token
        return claimsResolver.apply(claims);// here we are applying the function to the claims
               // we used function claimsResolver to make the method more generic so that we can use it to extract any claim fromthe token
    }


    private Claims extractAllClaims(String token) { // this is a helper method to extract all claims from the token
        return Jwts                             // this is a class provided by jjwt library to work with JWT tokens
                .parser()
                .setSigningKey(getSignKey())// it is used for setting the signing key to verify the token
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, UserDetails userDetails) { // userdeatils is an interface provided by spring security for user information it provides various methods to get user information
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }



    public String GenerateToken(String username){ // we use this funcion for generating the token for the user and we are passing the username as a parameter because we want to set the username as the subject of the token
        Map<String, Object> claims = new HashMap<>();// this is to store any additional information that we want to add to the token as claims
        return createToken(claims, username);
    }



    private String createToken(Map<String, Object> claims, String username) {// this function is used to create the token by taking the claims and the username as parameters
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*1))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}