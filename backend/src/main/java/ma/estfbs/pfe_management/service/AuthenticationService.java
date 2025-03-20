package ma.estfbs.pfe_management.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.var;
import ma.estfbs.pfe_management.dto.LoginRequest;
import ma.estfbs.pfe_management.dto.AuthResponse;
import ma.estfbs.pfe_management.dto.RegisterRequest;
import ma.estfbs.pfe_management.model.Utilisateur;
import ma.estfbs.pfe_management.repository.UtilisateurRepository;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest registerRequest) {
        var utilisateur = Utilisateur.builder()
        .nom(registerRequest.getNom())
        .prenom(registerRequest.getPrenom())
        .email(registerRequest.getEmail())
        .cin(registerRequest.getCin())
        .cne(registerRequest.getCne())
        .dateNaissance(registerRequest.getDateNaissance())
        .motDePasse(passwordEncoder.encode(registerRequest.getMotDePasse()))
        .role(registerRequest.getRole())
        .build();
        utilisateurRepository.save(utilisateur);
        var jwtToken = jwtService.generateToken(utilisateur);
        return AuthResponse.builder().token(jwtToken).build();
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var utilisateur = utilisateurRepository.findByEmail(request.getEmail()).orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        var jwtToken = jwtService.generateToken(utilisateur);
        return AuthResponse.builder().token(jwtToken).build();
    
}
    
}
