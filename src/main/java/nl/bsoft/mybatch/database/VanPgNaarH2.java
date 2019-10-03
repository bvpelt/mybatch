package nl.bsoft.mybatch.database;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity(name = "VANPGNAARH2")
@Table(name = "VANPGNAARH2")
public @Data
class VanPgNaarH2 {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "DATUMSTARTED")
    private LocalDate started;

    @Column(name = "LASTID")
    private Long lastId;
}
