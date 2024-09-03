package com.freightbroker.user_service.service;
import com.freightbroker.user_service.entity.CustomerDTO;
import com.freightbroker.user_service.entity.ProviderDTO;
import com.freightbroker.user_service.grpc.AuthenticationServiceGrpc;
import com.freightbroker.user_service.grpc.Identity;
import io.grpc.Status;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
public class AuthenticationServiceImpl extends AuthenticationServiceGrpc.AuthenticationServiceImplBase {
    @Autowired
    CustomerService customerService;

    @Autowired
    ProviderService providerService;

    @Autowired
    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Autowired
    public void setProviderService(ProviderService providerService) {
        this.providerService = providerService;
    }

    @Override
    public void authenticate(Identity.AuthenticationRequest request,
                             io.grpc.stub.StreamObserver<Identity.AuthenticationResponse> responseObserver) {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (request.getUserType().trim().equalsIgnoreCase("Comerciant")) {
            Optional<CustomerDTO> data = customerService.findByEmail(request.getEmail());
            if (data.isPresent() && encoder.matches(request.getPassword(), data.get().getParola())) {
                String jws = Jwts.builder()
                        .claim("id_customer", data.get().getId_customer())
                        .claim("email", data.get().getEmail())
                        .claim("user_type","comerciant")
                        .setIssuedAt(Date.from(Instant.now()))
                        .setExpiration(Date.from(Instant.now().plusSeconds(3600 * 3)))
                        .signWith(
                                SignatureAlgorithm.HS256, Base64.getDecoder().decode("Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=")
                        )
                        .compact();
                Claims claims = validateToken(jws);

                if (claims != null) {
                    Identity.AuthenticationResponse result = Identity.AuthenticationResponse.newBuilder().setToken(jws).build();
                    responseObserver.onNext(result);
                    responseObserver.onCompleted();
                } else {
                    responseObserver.onError(Status.UNAUTHENTICATED.withDescription("Token validation failed").asRuntimeException());
                }
            }else {
                responseObserver.onError(Status.UNAUTHENTICATED.withDescription("Token validation failed").asRuntimeException());
            }

        } else if (request.getUserType().trim().equalsIgnoreCase("Transportator")) {
            Optional<ProviderDTO> data = providerService.findByEmail(request.getEmail());
            if (data.isPresent() && encoder.matches(request.getPassword(), data.get().getParola())) {
                String jws = Jwts.builder()
                        .claim("id_customer", data.get().getId_customer())
                        .claim("email", data.get().getEmail())
                        .claim("user_type", "transportator")
                        .setIssuedAt(Date.from(Instant.now()))
                        .setExpiration(Date.from(Instant.now().plusSeconds(3600 * 3)))
                        .signWith(
                                SignatureAlgorithm.HS256, Base64.getDecoder().decode("Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=")
                        )
                        .compact();

                Claims claims = validateToken(jws);

                if (claims != null) {
                    Identity.AuthenticationResponse result = Identity.AuthenticationResponse.newBuilder().setToken(jws).build();
                    responseObserver.onNext(result);
                    responseObserver.onCompleted();
                } else {
                    responseObserver.onError(Status.UNAUTHENTICATED.withDescription("Token validation failed").asRuntimeException());
                }

            } else {
                responseObserver.onError(Status.UNAUTHENTICATED.withDescription("Token validation failed").asRuntimeException());
            }
        }
    }

    private Claims validateToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(Base64.getDecoder().decode("Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E="))
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            return null;
        }
    }

    @Override
    public void validate(
            Identity.ValidateRequest request,
            io.grpc.stub.StreamObserver<Identity.ValidateResponse> responseObserver) {
        String tokenToValidate = request.getValid();
        Claims claims = validateToken(tokenToValidate);
        if (claims != null) {
            Identity.ValidateResponse result = Identity.ValidateResponse.newBuilder().setValid("Token is valid").build();
            responseObserver.onNext(result);
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(Status.UNAUTHENTICATED.withDescription("Invalid token").asRuntimeException());
        }
    }

}
