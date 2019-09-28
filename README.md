# Building
Make sure you meet the [Prerequisits](##Prerequisits)

For building the complete project execute the following command:
```bash
mvn clean package site
```
It will generate the package and the site information.
## Prerequisits
In order to use this project you need to have
- postgres 10 installed and running with database mybatch and user testuser with password 12345


# Database config
See http://www.javaoptimum.com/how-to-configure-multiple-datasources-with-spring-boot/

# Spring boot
- https://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/htmlsingle/

# Spring Batch
- https://docs.spring.io/spring-batch/4.0.x/reference/html/job.html#javaConfig

# Spring Security
Default wordt gebruik gemaakt van:
- username: user
- password generated

# Logback
- patterns https://logback.qos.ch/manual/layouts.html

# Testen
- https://testingneeds.wordpress.com/tag/cucumberoptions/

# Coverage
jacco reports and maven
- https://stackoverflow.com/questions/21048776/maven-site-plugin-does-not-include-jacoco-reports
- https://www.petrikainulainen.net/programming/maven/creating-code-coverage-reports-for-unit-and-integration-tests-with-the-jacoco-maven-plugin/

# Maven
- https://maven.apache.org/pom.html 
- life cycle https://maven.apache.org/guides/introduction/introduction-to-the-lifecycle.html



###
- https://stackoverflow.com/questions/6078009/how-to-get-access-to-job-parameters-from-itemreader-in-spring-batch