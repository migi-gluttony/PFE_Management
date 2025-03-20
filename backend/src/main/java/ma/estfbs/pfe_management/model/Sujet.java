package ma.estfbs.pfe_management.model;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
class Sujet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;
    private String theme;
    private String description;

    @ManyToOne
    @JoinColumn(name = "classe_id", nullable = false)
    private Classe classe;
}
