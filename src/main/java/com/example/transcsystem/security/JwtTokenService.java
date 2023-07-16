package com.example.transcsystem.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Samwel Wafula
 * Created on July 09, 2023.
 * Time 3:16 PM
 */

@Service
public class JwtTokenService {

    @Autowired
    private CustomUserDetails customUserDetails;

    String genSec = "XfxsbjC7Wkc,V~Xj3?T{s#\"82vf88.Wh";

    ObjectMapper objectMapper = new ObjectMapper();

    public Object genToken() {
        try {

            Object object = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String userId = "";
            if (object instanceof UserDetails userDetails) {
                userId = userDetails.getUsername();

            } else {
                userId = object.toString();
            }

            String finalUserId = userId;
            Map<String, String> data = new HashMap<>() {{
                put("user_id", finalUserId);
            }};

            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(userId)
                    .expirationTime(new Date(new Date().getTime() + 60 * 60 * 1000))
                    .build();

            SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);

            JWSSigner signer = new MACSigner(genSec);
            signedJWT.sign(signer);

            String token = signedJWT.serialize();

            return new HashMap<String, String>() {{
                put("access_token", token);
                put("token-type", "Bearer");
            }};
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Map<String, String> verifyToken(String token, HttpServletRequest request) {
        try {
            SignedJWT jwsObject = SignedJWT.parse(token);
            JWSVerifier verifier = new MACVerifier(genSec);
            boolean isValid = jwsObject.verify(verifier);

            if (isValid && new Date().before(jwsObject.getJWTClaimsSet().getExpirationTime())) {
                String uaerId = jwsObject.getJWTClaimsSet().getSubject();

                UserDetails userDetails = customUserDetails.loadUserByUsername(uaerId);

                if (userDetails.isEnabled()) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            userDetails.getPassword(),
                            userDetails.getAuthorities()
                    );

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
                return new HashMap<>() {{
                    put("status", "VALID");
                    put("data", jwsObject.getPayload().toString());
                }};
            } else {
                SecurityContextHolder.clearContext();
                return new HashMap<>() {{
                    put("status", "INVALID");
                }};
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new HashMap<>() {{
            put("status", "INVALID");
        }};
    }
}
