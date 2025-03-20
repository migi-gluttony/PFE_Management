package ma.estfbs.pfe_management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import ma.estfbs.pfe_management.dto.RegisterRequest;
import ma.estfbs.pfe_management.dto.RegisterResponse;

import ma.estfbs.pfe_management.service.UtilisateurService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UtilisateurService userService;


    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> registerUser(@RequestBody RegisterRequest registerRequest) {
        // Delegate the registration logic to the UserService
        RegisterResponse response = userService.registerUser(registerRequest);

        // Return the response with a 200 OK status
        return ResponseEntity.ok(response);
    }
}