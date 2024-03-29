package com.itgate.springjwt.controllers;

import com.itgate.springjwt.exception.TokenRefreshException;
import com.itgate.springjwt.models.*;
import com.itgate.springjwt.payload.request.LoginRequest;
import com.itgate.springjwt.payload.request.SignupRequest;
import com.itgate.springjwt.payload.request.TokenRefreshRequest;
import com.itgate.springjwt.payload.response.JwtResponse;
import com.itgate.springjwt.payload.response.MessageResponse;
import com.itgate.springjwt.payload.response.TokenRefreshResponse;
import com.itgate.springjwt.repository.RoleRepository;
import com.itgate.springjwt.repository.UserRepository;
import com.itgate.springjwt.security.jwt.JwtUtils;
import com.itgate.springjwt.security.services.RefreshTokenService;
import com.itgate.springjwt.security.services.UserDetailsImpl;
import com.itgate.springjwt.utils.EmailService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")

public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    RefreshTokenService refreshTokenService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        //test confim si true il autorise l'accée si nn maunajamch ya3ml login

        Optional<User> u = userRepository.findByUsername(loginRequest.getUsername());
        if (u.get().getConfirm() == true) {

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(item -> item.getAuthority())
                    .collect(Collectors.toList());
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
            return ResponseEntity.ok(new JwtResponse(jwt,refreshToken.getToken(),
                    userDetails.getId(),
                    userDetails.getUsername(),
                    userDetails.getEmail(),
                    roles, true));
        } else {
throw  new RuntimeException("user not confirmed");
        }
    }
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                   
                    case "customer":
                        Role custRole = roleRepository.findByName(ERole.ROLE_CUSTOMER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(custRole);

                        break;

                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        user.setConfirm(true);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @PostMapping("/forgetpassword")
    public HashMap<String, String> resetpassword(String email) {
        HashMap message = new HashMap();
        User userexisting = userRepository.findByEmail(email);
        if (userexisting == null) {
            message.put("user", "user not found");
            return message;
        }
        UUID token = UUID.randomUUID();
        userexisting.setPasswordresettoken(token.toString());
        userexisting.setId(userexisting.getId());
        Mail mail = new Mail();
        mail.setContent("votre nouveau token est " + "http://localhost:8088/api/auth/savepassword/" + userexisting.getPasswordresettoken());
        mail.setFrom("dolisha095@gmail.com");
        mail.setTo(userexisting.getEmail());
        mail.setSubject("reset password");
        emailService.sendSimpleMessage(mail);
        userRepository.saveAndFlush(userexisting);
        message.put("user", "userfound");
        return message;
    }

    /////////////////////////////mot pass nouveau
    @PostMapping("/savepassword/{passwordresettoken}")
    public HashMap<String, String> savepassword(@PathVariable String passwordresettoken, String newpassword) {
        HashMap message = new HashMap();
        User userexisting = userRepository.findByPasswordresettoken(passwordresettoken);
        if (userexisting != null) {
            userexisting.setId(userexisting.getId());
            userexisting.setPassword(new BCryptPasswordEncoder().encode(newpassword));
            userexisting.setPasswordresettoken(null);
            userRepository.save(userexisting);
            message.put("resetpassword", "proccesed");
            return message;
        } else {
            message.put("resetpassword", "failed");
            return message;
        }


    }
    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtUtils.generateTokenFromUsername(user.getUsername());
                    return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh token is not in database!"));
    }
    
    
    //to fund by password i using this in log to profil in frant
    
    @GetMapping("/users/{username}")
    public ResponseEntity<Optional<User>> getUserByUsername(@PathVariable String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }



}
