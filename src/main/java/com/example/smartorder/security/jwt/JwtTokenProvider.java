package com.example.smartorder.security.jwt;

import com.example.smartorder.member.domain.Member;
import com.example.smartorder.security.AccessorService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {
    public static final String SCHEME = "Bearer";

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration.access}")
    private long accessTokenExpiration;

    private final AccessorService accessorService;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(this.secretKey.getBytes());
    }

    public String generateToken(Member member) {
        Map<String, Object> claims = new HashMap<>();

        claims.put("role", member.getRole().toString());
        claims.put("accessId", member.getAccessId());

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setClaims(claims)
                .setSubject(member.getId())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + this.accessTokenExpiration))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        String id = Jwts.parser()
                .setSigningKey(this.secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        UserDetails accessor = accessorService.loadUserByUsername(id);

        return new UsernamePasswordAuthenticationToken(accessor, "", accessor.getAuthorities());
    }

    public String resolveToken(HttpServletRequest request) {
        return request.getHeader(HttpHeaders.AUTHORIZATION);
    }

    public boolean validateToken(String token) {
        try {
            Date tokenExpiration = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody()
                    .getExpiration();

            Date now = new Date();
            return !tokenExpiration.before(now);
        } catch (Exception e) {
            return false;
        }
    }
}
