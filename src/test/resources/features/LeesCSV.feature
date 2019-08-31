#language:nl
#
#
# Controleer lezen van csv file
Functionaliteit: Lees uit csv file

  Scenario: Lees uit csv file
    Gegeven inputbestand "Beschikkingsbevoegdheid.csv" bestaat op classpath
    Als de gegevens gelezen zijn
    Dan zijn er "9" gegevens gelezen
