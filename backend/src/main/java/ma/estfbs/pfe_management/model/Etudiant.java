package ma.estfbs.pfe_management.model;
import jakarta.persistence.*;
import lombok.*;
@Entity
@Data
@NoArgsConstructor
class Etudiant {

    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "classe_id", nullable = false)
    private Classe classe;
}