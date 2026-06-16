package com.tfb.service;

import com.tfb.config.JwtUtil;
import com.tfb.dto.AuthRequest;
import com.tfb.dto.AuthResponse;
import com.tfb.entity.User;
import com.tfb.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepo;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;

    public AuthResponse register(AuthRequest req) {
        if (userRepo.findByName(req.getName()).isPresent())
            throw new RuntimeException("Name already taken");
        User u = new User();
        u.setName(req.getName());
        u.setPasswordHash(encoder.encode(req.getPassword()));
        u.setRole(User.Role.valueOf(req.getRole().toUpperCase()));
        userRepo.save(u);
        return new AuthResponse(jwtUtil.generate(u.getName()), u.getName(), u.getRole().name());
    }

    public AuthResponse login(AuthRequest req) {
        User u = userRepo.findByName(req.getName())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));
        if (!encoder.matches(req.getPassword(), u.getPasswordHash()))
            throw new RuntimeException("Invalid credentials");
        return new AuthResponse(jwtUtil.generate(u.getName()), u.getName(), u.getRole().name());
    }
}
