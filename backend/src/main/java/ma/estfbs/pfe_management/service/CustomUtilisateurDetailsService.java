package ma.estfbs.pfe_management.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ma.estfbs.pfe_management.model.Utilisateur;
import ma.estfbs.pfe_management.repository.UtilisateurRepository;


@Service
@RequiredArgsConstructor
public class CustomUtilisateurDetailsService implements UserDetailsService {
    private final UtilisateurRepository utilisateurRepository;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Optional<Utilisateur> utilisateur = utilisateurRepository.findByEmail(email);
        // utilisateur.orElseThrow(() -> new UsernameNotFoundException("User not found"));
        // String password = utilisateur.get().getMotDePasse();
        // String role = utilisateur.get().getRole().name();
        // List<GrantedAuthority> roles = new ArrayList<>();
        // roles.add(new SimpleGrantedAuthority(role));       
        // return new Utilisateur(email, password, roles);
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    return utilisateur;


    }
}
