package nl.bsoft.mybatch.csv;

import java.time.LocalDate;

public class Gegeven {
    private Long code;
    private String waarde;
    private LocalDate datumVanAf;
    private LocalDate datumTot;
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
