package ma.estfbs.pfe_management.service;

import java.text.Normalizer;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
// Remove the import lombok.var; line
import ma.estfbs.pfe_management.dto.LoginRequest;
import ma.estfbs.pfe_management.dto.PasswordResetConfirm;
import ma.estfbs.pfe_management.dto.PasswordResetRequest;
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

    /**
     * Generates standardized email for users based on their first and last name
     * Format: prenomnom.efb@usms.ac.ma
     */
    public String generateEmail(String prenom, String nom) {
        // Remove accents and special characters
        String normalizedPrenom = Normalizer.normalize(prenom.toLowerCase(), Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "");
        String normalizedNom = Normalizer.normalize(nom.toLowerCase(), Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "");
        
        // Remove spaces
        normalizedPrenom = normalizedPrenom.replaceAll("\\s+", "");
        normalizedNom = normalizedNom.replaceAll("\\s+", "");
        
        return normalizedPrenom + normalizedNom + ".efb@usms.ac.ma";
    }

    /**
     * Modified register method to generate email automatically
     */
    public AuthResponse register(RegisterRequest registerRequest) {
        // Generate email from nom and prenom
        String email = generateEmail(registerRequest.getPrenom(), registerRequest.getNom());
        
        // Set the generated email
        registerRequest.setEmail(email);
        
        // Check for existing email
        if (utilisateurRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already registered");
        }
        
        // Check for existing CIN (for non-students)
        if (registerRequest.getRole() != Utilisateur.Role.ETUDIANT && registerRequest.getCni() != null) {
            if (utilisateurRepository.existsByCni(registerRequest.getCni())) {
                throw new RuntimeException("CIN already registered");
            }
        }
        
        // Check for existing CNE (for students)
        if (registerRequest.getRole() == Utilisateur.Role.ETUDIANT && registerRequest.getCne() != null) {
            if (utilisateurRepository.existsByCne(registerRequest.getCne())) {
                throw new RuntimeException("CNE already registered");
            }
        }
        
        // Replace var with Utilisateur
        Utilisateur utilisateur = Utilisateur.builder()
            .nom(registerRequest.getNom())
            .prenom(registerRequest.getPrenom())
            .email(email) // Use the generated email
            .cni(registerRequest.getCni())
            .cne(registerRequest.getCne())
            .dateNaissance(registerRequest.getDateNaissance())
            .motDePasse(passwordEncoder.encode(registerRequest.getMotDePasse()))
            .role(registerRequest.getRole())
            .build();
        
        utilisateurRepository.save(utilisateur);
        // Replace var with String
        String jwtToken = jwtService.generateToken(utilisateur);
        return AuthResponse.builder().token(jwtToken).build();
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        // Replace var with Utilisateur
        Utilisateur utilisateur = utilisateurRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        // Replace var with String
        String jwtToken = jwtService.generateToken(utilisateur);
        return AuthResponse.builder().token(jwtToken).build();
    }
    
    /**
     * First step of password reset - verify user info and generate token
     */
    public AuthResponse requestPasswordReset(PasswordResetRequest request) {
        // Find the user
        Utilisateur utilisateur;
        
        // Check based on role-specific identification
        if (request.getCne() != null && !request.getCne().isEmpty()) {
            // Student identification via CNE
            utilisateur = utilisateurRepository.findByEmailAndDateNaissanceAndCne(
                    request.getEmail(), request.getDateNaissance(), request.getCne())
                    .orElseThrow(() -> new RuntimeException("Invalid user information"));
        } else if (request.getCni() != null && !request.getCni().isEmpty()) {
            // Staff identification via CIN
            utilisateur = utilisateurRepository.findByEmailAndDateNaissanceAndCni(
                    request.getEmail(), request.getDateNaissance(), request.getCni())
                    .orElseThrow(() -> new RuntimeException("Invalid user information"));
        } else {
            throw new RuntimeException("Missing identification information");
        }
        
        // Generate password reset token
        String resetToken = jwtService.generatePasswordResetToken(utilisateur.getEmail());
        
        return AuthResponse.builder().token(resetToken).build();
    }

    /**
     * Second step of password reset - verify token and update password
     */
    public AuthResponse confirmPasswordReset(PasswordResetConfirm request) {
        // Validate token
        String email = jwtService.extractUsername(request.getToken());
        if (email == null || email.isEmpty()) {
            throw new RuntimeException("Invalid token");
        }
        
        // Find the user
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Check if token is valid
        if (!jwtService.isTokenValid(request.getToken(), utilisateur)) {
            throw new RuntimeException("Invalid or expired token");
        }
        
        // Update password
        utilisateur.setMotDePasse(passwordEncoder.encode(request.getNewPassword()));
        utilisateurRepository.save(utilisateur);
        
        // Generate a new regular token for the user
        String newToken = jwtService.generateToken(utilisateur);
        
        return AuthResponse.builder().token(newToken).build();
    }
}