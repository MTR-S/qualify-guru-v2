package com.dev.mtrs.projects.qualifyguruv2.identity.internal.adapters.in;

import com.dev.mtrs.projects.qualifyguruv2.identity.internal.adapters.out.UserEntity;
import com.dev.mtrs.projects.qualifyguruv2.identity.internal.adapters.out.UserMapper;
import com.dev.mtrs.projects.qualifyguruv2.identity.internal.adapters.out.UserRepository;
import com.dev.mtrs.projects.qualifyguruv2.identity.internal.domain.AuthRequest;
import com.dev.mtrs.projects.qualifyguruv2.identity.internal.domain.AuthResponse;
import com.dev.mtrs.projects.qualifyguruv2.identity.internal.ports.out.AuthTokenPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

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

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserEntity user = userOptional.get();

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = authToken.generateToken(user.getId());

        return ResponseEntity.ok(new AuthResponse(token));
    }
}
