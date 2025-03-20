package ma.estfbs.pfe_management.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ma.estfbs.pfe_management.model.Utilisateur;

public interface UtilisateurRepository extends JpaRepository<Utilisateur,Long>{
    Optional<Utilisateur> findByEmail(String email);
}
