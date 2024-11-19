package com.module5.zingMp3Clone.Security;

import com.module5.zingMp3Clone.Exception.ExceptionValue;
import com.module5.zingMp3Clone.Model.Entity.RoleEntity;
import com.module5.zingMp3Clone.Model.Entity.UserEntity;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.KeyLengthException;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class GeneratorToken {
    @Value(value = "${jwt.SINGER_KEY}")
    private String SINGER_KEY;

    public String generatorToken(UserEntity user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .jwtID(UUID.randomUUID().toString())
                .expirationTime(new Date(System.currentTimeMillis() + 86400 * 1000L))
                .subject(user.getUsername())
                .claim("scope", buildScope(user.getRoles()))
                .build();
        JWSObject jwsObject = new JWSObject(header, new Payload(claimsSet.toJSONObject()));
        try {
            jwsObject.sign(new MACSigner(SINGER_KEY.getBytes()));
        } catch (JOSEException e) {
            throw new RuntimeException(ExceptionValue.FAILED_GENERATOR_TOKEN.getValue());
        }
        return jwsObject.serialize();
    }

    public String buildScope(List<RoleEntity> roles) {
        return roles.stream()
                .map(RoleEntity::getCode)
                .collect(Collectors.joining(" "));
    }
}
