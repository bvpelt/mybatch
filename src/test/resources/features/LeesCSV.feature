#language:nl
#
#
# Controleer lezen van csv file
Functionaliteit: Lees uit csv files

  Scenario: Lees aantal entries uit uit csv file
    Gegeven database is geinitialiseerd
    En inputbestand "Beschikkingsbevoegdheid.csv" bestaat op classpath
    Als de gegevens gelezen zijn uit bestand "Beschikkingsbevoegdheid.csv"
    Dan zijn er 9 gegevens vastgelegd

  Scenario: Voer verwerking uit
    Gegeven database is geinitialiseerd
    Als job "csvtopostgres" met success gedraaid heeft voor bestand "Beschikkingsbevoegdheid.csv"
    Dan zijn er 9 gegevens vastgelegd


  Scenario: Voer verwerking uit met 1 fout in invoerbestand met 10 records
    Gegeven database is geinitialiseerd
    Als job "csvtopostgres" met success gedraaid heeft voor bestand "BeschikkingF01.csv"
    Dan zijn er 9 gegevens vastgelegd

  Scenario: Voer verwerking uit met 2 fouten in invoerbestand met 10 records
    Gegeven database is geinitialiseerd
    Als job "csvtopostgres" met success gedraaid heeft voor bestand "BeschikkingF02.csv"
    Dan zijn er 9 gegevens vastgelegd