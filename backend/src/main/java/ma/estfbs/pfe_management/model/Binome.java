package ma.estfbs.pfe_management.model;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
class Binome {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "etudiant1_id", nullable = false)
    private Utilisateur etudiant1;

    @ManyToOne
    @JoinColumn(name = "etudiant2_id")
    private Utilisateur etudiant2;

    @ManyToOne
    @JoinColumn(name = "encadrant_id", nullable = false)
    private Utilisateur encadrant;

    @ManyToOne
    @JoinColumn(name = "sujet_id", nullable = false)
    private Sujet sujet;
}
