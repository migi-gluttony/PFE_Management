package ma.estfbs.pfe_management.model;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Data
@NoArgsConstructor
class Rapport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "binome_id", nullable = false)
    private Binome binome;

    private String titre;
    private String localisationRapport;
    private Long note;
    private String commentaire;
}