package nl.bsoft.mybatch.database;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity(name = "BeschikkingsBevoegdheid")
@Table(name = "BESCHIKKINGSBEVOEGDHEID")
public @Data
class BeschikkingsBevoegdheid {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "CODE")
    private Long code;

    @Column(name = "WAARDE")
    private String waarde;

    @Column(name = "DATUMVANAF")
    private LocalDate datumVanAf;

    @Column(name = "DATUMTOT")
    private LocalDate datumTot;

    @Column(name = "TOELICHTING")
    private String toelichting;
}
