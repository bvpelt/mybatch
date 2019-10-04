package nl.bsoft.mybatch.csv;

import lombok.Data;

import java.time.LocalDate;

public @Data
class Gegeven {
    private Long code;
    private String waarde;
    private LocalDate datumVanAf;
    private LocalDate datumTot;
    private String toelichting;
}
