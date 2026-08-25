package com.dev.mtrs.projects.qualifyguruv2.identity.internal.adapters.in;

import com.dev.mtrs.projects.qualifyguruv2.identity.internal.adapters.out.UserEntity;
import com.dev.mtrs.projects.qualifyguruv2.identity.internal.adapters.out.UserMapper;
import com.dev.mtrs.projects.qualifyguruv2.identity.internal.adapters.out.UserRepository;
import com.dev.mtrs.projects.qualifyguruv2.identity.internal.domain.AuthRequest;
import com.dev.mtrs.projects.qualifyguruv2.identity.internal.domain.AuthResponse;
import com.dev.mtrs.projects.qualifyguruv2.identity.internal.ports.out.AuthTokenPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthTokenPort authToken;
    private final UserMapper userMapper;

    public AuthController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          AuthTokenPort authToken,
                          UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authToken = authToken;
        this.userMapper = userMapper;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AuthRequest request) {

        if (userRepository.findByEmail(request.email()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already in use.");
        }

        UserEntity newUser = userMapper.toEntity(request);
        userRepository.save(newUser);

        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully.");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        Optional<UserEntity> userOptional = userRepository.findByEmail(request.email());

        if (userOptional.isEmpty() || !passwordEncoder.matches(request.password(), userOptional.get().getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserEntity user = userOptional.get();
        String token = authToken.generateToken(user.getId());

        ResponseCookie jwtCookie = ResponseCookie.from("access_token", token)
                .httpOnly(true)
                .secure(false)        // Needs to be defined to TRUE in production when using HTTPS
                .path("/")
                .maxAge(Duration.ofDays(1))
                .sameSite("Strict")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(new AuthResponse("Login successful"));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        ResponseCookie deleteCookie = ResponseCookie.from("access_token", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .sameSite("Strict")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, deleteCookie.toString())
                .build();
    }
}
