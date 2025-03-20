package ma.estfbs.pfe_management.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
class Classe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String filiere;
    private Long semestre;
    private Long anneeScolaire;

    @ManyToOne
    @JoinColumn(name = "departement_id", nullable = false)
    private Departement departement;
}