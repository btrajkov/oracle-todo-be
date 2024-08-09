package oracle.todo.service;

import jakarta.enterprise.context.RequestScoped;
import oracle.todo.model.json.CustomerJSON;
import oracle.todo.util.TokenUtils;
import org.eclipse.microprofile.jwt.Claims;
import org.jose4j.jwt.JwtClaims;

import java.util.Arrays;

@RequestScoped
public class TokenService {

    public String generate(CustomerJSON CustomerJSON) throws Exception {

        JwtClaims jwtClaims = new JwtClaims();
        jwtClaims.setIssuer("https://quarkus.io/using-jwt-rbac");
        jwtClaims.setJwtId("a-123");
        jwtClaims.setSubject(CustomerJSON.getMail());
        jwtClaims.setClaim(Claims.upn.name(), CustomerJSON.getMail());
        jwtClaims.setClaim(Claims.preferred_username.name(), CustomerJSON.getUsername());
        jwtClaims.setClaim(Claims.full_name.name(), CustomerJSON.getName());
        jwtClaims.setClaim(Claims.groups.name(), Arrays.asList(TokenUtils.ROLE_USER));
        jwtClaims.setAudience("using-jwt");
        jwtClaims.setExpirationTimeMinutesInTheFuture(60);

        String token = TokenUtils.generateTokenString(jwtClaims);

        return token;
    }
}
