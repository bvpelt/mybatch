#language:nl
#
#
# Controleer lezen van csv file
Functionaliteit: Lees uit csv files

  Scenario: Lees aantal entries uit uit csv file
    Gegeven database is geinitialiseerd
    En inputbestand "Beschikkingsbevoegdheid.csv" bestaat op classpath
    Als de gegevens gelezen zijn uit bestand met skip "Beschikkingsbevoegdheid.csv"
    Dan zijn er 9 gegevens vastgelegd

  Scenario: Voer verwerking uit op Beschikkingsbevoegdheid.csv skip
    Gegeven database is geinitialiseerd
    Als job "csvtopostgresskip" met success gedraaid heeft voor bestand "Beschikkingsbevoegdheid.csv"
    Dan zijn er 9 gegevens vastgelegd


  Scenario: Voer verwerking uit met 1 fout in invoerbestand met 10 records skip
    Gegeven database is geinitialiseerd
    Als job "csvtopostgresskip" met success gedraaid heeft voor bestand "BeschikkingF01.csv"
    Dan zijn er 9 gegevens vastgelegd

  Scenario: Voer verwerking uit met 2 fouten in invoerbestand met 11 records skip
    Gegeven database is geinitialiseerd
    Als job "csvtopostgresskip" met success gedraaid heeft voor bestand "BeschikkingF02.csv"
    Dan zijn er 9 gegevens vastgelegd

  Scenario: Voer verwerking uit met 3 fouten in invoerbestand met 12 records skip
    Gegeven database is geinitialiseerd
    Als job "csvtopostgresskip" met success gedraaid heeft voor bestand "BeschikkingF03.csv"
    Dan zijn er 0 gegevens vastgelegd


  Scenario: Voer verwerking uit met 1 fout in invoerbestand met 10 records limit
    Gegeven database is geinitialiseerd
    Als job "csvtopostgreslimit" met success gedraaid heeft voor bestand "BeschikkingF01.csv"
    Dan zijn er 9 gegevens vastgelegd

  Scenario: Voer verwerking uit met 2 fouten in invoerbestand met 11 records limit
    Gegeven database is geinitialiseerd
    Als job "csvtopostgreslimit" met success gedraaid heeft voor bestand "BeschikkingF02.csv"
    Dan zijn er 9 gegevens vastgelegd

  Scenario: Voer verwerking uit met 3 fouten in invoerbestand met 12 records limit
    Gegeven database is geinitialiseerd
    Als job "csvtopostgreslimit" met success gedraaid heeft voor bestand "BeschikkingF03.csv"
    Dan zijn er 0 gegevens vastgelegd

  Scenario: Voer verwerking uit op BRPLand.csv
    Gegeven database is geinitialiseerd
    Als job "csvtopostgresskip" met success gedraaid heeft voor bestand "BRPLand.csv"
    Dan zijn er 387 gegevens vastgelegd

  Scenario: Voer verwerking uit op KadastraleGemeente.csv
    Gegeven database is geinitialiseerd
    Als job "csvtopostgreslimit" met success gedraaid heeft voor bestand "KadastraleGemeente.csv"
    Dan zijn er 40 gegevens vastgelegd
