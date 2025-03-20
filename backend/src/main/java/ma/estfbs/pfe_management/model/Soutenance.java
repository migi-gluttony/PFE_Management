package ma.estfbs.pfe_management.model;
import java.sql.Date;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
class Soutenance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date date;
    private String heure;

    @ManyToOne
    @JoinColumn(name = "binome_id", nullable = false)
    private Binome binome;
}