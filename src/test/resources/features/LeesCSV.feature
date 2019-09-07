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
