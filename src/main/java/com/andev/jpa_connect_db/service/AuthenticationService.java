package com.andev.jpa_connect_db.service;

import com.andev.jpa_connect_db.dto.request.AuthenticationRequest;
import com.andev.jpa_connect_db.dto.request.IntrospectRequest;
import com.andev.jpa_connect_db.dto.response.AuthenticationResponse;
import com.andev.jpa_connect_db.dto.response.IntrospectResponse;
import com.andev.jpa_connect_db.exception.AppException;
import com.andev.jpa_connect_db.exception.ErrorCode;
import com.andev.jpa_connect_db.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserRepository userRepository;

    @NonFinal
    @Value("${jwt.signkey}")
    protected String SIGNER_KEY;

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) throws JOSEException {
        var user = userRepository.findByUsername(authenticationRequest.getUsername()).orElseThrow(()->new AppException(ErrorCode.USER_NOT_EXIST, authenticationRequest.getUsername()));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

        boolean authenticated = passwordEncoder.matches(authenticationRequest.getPassword(), user.getPassword());
        if(!authenticated){
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        String token = genToken(user.getUsername());

        return AuthenticationResponse.builder()
                .authenticated(authenticated)
                .token(token)
                .build();
    }

    private String genToken(String userName) {
        // Xac dinh header
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS256);
        //Xac dinh payload
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(userName)
                .issuer("anhq9")
                .issueTime(new Date())
                .expirationTime(new Date(new Date().getTime() + 1000 *60*60))
                .claim("customClaim", "Custom")
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(jwsHeader, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        }catch (JOSEException exception){
            log.error("Cannot create token ", exception.getMessage());
            throw new RuntimeException(exception);
        }
    }

    public IntrospectResponse introspectResponse(IntrospectRequest request) throws JOSEException, ParseException {
        var token = request.getToken();
        JWSVerifier jwsVerifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT object = SignedJWT.parse(token);
        boolean isValid = object.verify(jwsVerifier);
        Date expirationTime = object.getJWTClaimsSet().getExpirationTime();
        return IntrospectResponse.builder()
                .valid(isValid && expirationTime.after(new Date()))
                .build();
    }
}
