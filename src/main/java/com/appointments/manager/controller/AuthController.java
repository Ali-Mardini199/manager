package com.appointments.manager.controller;

import com.appointments.manager.security.JwtUtil;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

import com.appointments.manager.dto.AuthRequest;
import com.appointments.manager.dto.AuthResponse;
import com.appointments.manager.dto.SignupRequest;
import com.appointments.manager.model.Role;
import com.appointments.manager.model.User;
import com.appointments.manager.repository.RoleRepository;
import com.appointments.manager.repository.UserRepository;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

        @Autowired
        private AuthenticationManager authenticationManager;

        @Autowired
        private JwtUtil jwtUtil;

        @Autowired
        private CustomUserDetailsService userDetailsService;

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private RoleRepository roleRepository;

        @PostMapping("/login")
        public ResponseEntity<?> login(@RequestBody AuthRequest request) {
                authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(
                                                request.getUsername(), request.getPassword()));
                UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
                String token = jwtUtil.generateToken(userDetails);
                return ResponseEntity.ok(new AuthResponse(token));
        }

        @PostMapping("/signup")
        public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest request) {
                if (userRepository.existsByEmail(request.getEmail())) {
                        return ResponseEntity
                                        .status(HttpStatus.CONFLICT)
                                        .body("Email already in use");
                }

                if (userRepository.existsByUsername(request.getUsername())) {
                        return ResponseEntity
                                        .status(HttpStatus.CONFLICT)
                                        .body("Username already in use");
                }
                Role role = roleRepository.findByName("CUSTOMER").orElse(null);
                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                User user = new User();
                user.setUsername(request.getUsername());
                user.setFullName(request.getFullName());
                user.setEmail(request.getEmail());
                user.setPassword(passwordEncoder.encode(request.getPassword()));
                user.setRole(role);
                userRepository.save(user);
                return ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body("User registered successfully");
        }
}
