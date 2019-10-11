#language:nl

Functionaliteit: Lees uit csv files
  Gegevens worden uit csv file gelezen
  Geconverteerd naar csv java object
  Daarna wordt csv java object geconverteerd naar database java object
  Database java object wordt in database vastgelegd

  #
  # Initialiseer de database
  # Lees gegevens uit csv input bestand
  # Skip errors tijdens lezen uit csv bestand
  # Leg alle gelezen gegevens vast in de database
  #
  Scenario: Lees aantal entries uit uit csv file
    Gegeven database is geinitialiseerd
    En inputbestand "Beschikkingsbevoegdheid.csv" bestaat op classpath
    Als de gegevens gelezen zijn uit bestand met skip "Beschikkingsbevoegdheid.csv"
    Dan zijn er 9 gegevens vastgelegd

  #
  # Initialiseer de database
  # Verwerk de csv naar postgres job en skip error, voor bestand zonder fouten
  # Controleer dat alle gegevens uit input vastgelegd zijn
  #
  Scenario: Voer verwerking uit op Beschikkingsbevoegdheid.csv skip
    Gegeven database is geinitialiseerd
    Als job "csvtopostgresskip" met success gedraaid heeft voor bestand "Beschikkingsbevoegdheid.csv"
    Dan zijn er 9 gegevens vastgelegd

  #
  # Initialiseer de database
  # Verwerk de csv naar postgres job en skip error, voor bestand met 1 fout
  # Controleer dat alle gegevens uit input vastgelegd zijn
  #
  Scenario: Voer verwerking uit met 1 fout in invoerbestand met 10 records skip
    Gegeven database is geinitialiseerd
    Als job "csvtopostgresskip" met success gedraaid heeft voor bestand "BeschikkingF01.csv"
    Dan zijn er 9 gegevens vastgelegd

  #
  # Initialiseer de database
  # Verwerk de csv naar postgres job en skip error, voor bestand met 2 fouten
  # Controleer dat alle gegevens uit input vastgelegd zijn
  #
  Scenario: Voer verwerking uit met 2 fouten in invoerbestand met 11 records skip
    Gegeven database is geinitialiseerd
    Als job "csvtopostgresskip" met success gedraaid heeft voor bestand "BeschikkingF02.csv"
    Dan zijn er 9 gegevens vastgelegd

  #
  # Initialiseer de database
  # Verwerk de csv naar postgres job en skip error, voor bestand met 3 fouten
  # Controleer dat niets vastgelegd is, default worden 2 fouten toegestaan
  #
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
