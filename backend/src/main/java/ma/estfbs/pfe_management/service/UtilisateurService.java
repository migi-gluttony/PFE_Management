package ma.estfbs.pfe_management.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ma.estfbs.pfe_management.dto.RegisterRequest;
import ma.estfbs.pfe_management.dto.RegisterResponse;
import ma.estfbs.pfe_management.model.Utilisateur;
import ma.estfbs.pfe_management.repository.UtilisateurRepository;

@Service
public class UtilisateurService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Transactional
    public RegisterResponse registerUser(RegisterRequest registerRequest) {
        // Create a new Utilisateur object
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNom(registerRequest.getNom());
        utilisateur.setPrenom(registerRequest.getPrenom());
        utilisateur.setEmail(registerRequest.getEmail());
        utilisateur.setCin(registerRequest.getCin());
        utilisateur.setCne(registerRequest.getCne());
        utilisateur.setDateNaissance(registerRequest.getDateNaissance());
        utilisateur.setMotDePasse(passwordEncoder.encode(registerRequest.getMotDePasse())); // Encrypt the password
        utilisateur.setRole(registerRequest.getRole());

        // Save the user to the database
        Utilisateur savedUser = utilisateurRepository.save(utilisateur);

        // Create and return the response
        RegisterResponse response = new RegisterResponse();
        response.setMessage("User registered successfully");
        response.setUserId(savedUser.getId());

        return response;
    }
}
