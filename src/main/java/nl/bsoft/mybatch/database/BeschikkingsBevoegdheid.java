package nl.bsoft.mybatch.database;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity(name = "BeschikkingsBevoegdheid")
@Table(name = "BESCHIKKINGSBEVOEGDHEID")
public class BeschikkingsBevoegdheid {
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

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public String getWaarde() {
        return waarde;
    }

    public void setWaarde(String waarde) {
        this.waarde = waarde;
    }

    public LocalDate getDatumVanAf() {
        return datumVanAf;
    }

    public void setDatumVanAf(LocalDate datumVanAf) {
        this.datumVanAf = datumVanAf;
    }

    public LocalDate getDatumTot() {
        return datumTot;
    }

    public void setDatumTot(LocalDate datumTot) {
        this.datumTot = datumTot;
    }

    public String getToelichting() {
        return toelichting;
    }

    public void setToelichting(String toelichting) {
        this.toelichting = toelichting;
    }
}
