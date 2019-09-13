#language:nl
#
#
# Controleer lezen van csv file
Functionaliteit: Lees uit csv files

  Scenario: Lees aantal entries uit uit csv file
    Gegeven database is geinitialiseerd
    En inputbestand "Beschikkingsbevoegdheid.csv" bestaat op classpath
    Als de gegevens gelezen zijn
    Dan zijn er 9 gegevens gelezen

  Scenario: Voer verwerking uit
    Gegeven database is geinitialiseerd
    Als jobLauncher met success gedraaid heeft
    Dan zijn er 9 gegevens vastgelegd