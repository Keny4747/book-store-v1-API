package pe.todotic.bookstoreapi_s2.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.todotic.bookstoreapi_s2.security.TokenProvider;
import pe.todotic.bookstoreapi_s2.web.dto.AuthCredentials;

@RestController
@RequestMapping("/api")
public class JwtController {

    @Autowired
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    @Autowired
    private TokenProvider tokenProvider;
    @PostMapping("/authenticate")
    ResponseEntity<JwtToken> authenticate(@RequestBody AuthCredentials credentials) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                credentials.email(),
                credentials.password()
        );

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String accessToken = tokenProvider.createToken(authentication);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(new JwtToken(accessToken));
    }

    record JwtToken(String token){}
}
