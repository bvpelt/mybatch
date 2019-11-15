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
- http://java2novice.com/spring-boot/load-external-configuration-files/
- default configuration properties https://docs.spring.io/spring-boot/docs/current/reference/html/common-application-properties.html

# Spring Batch
- https://docs.spring.io/spring-batch/4.0.x/reference/html/job.html#javaConfig
- examples of the book springboot in action https://github.com/acogoluegnes/Spring-Batch-in-Action

# Spring Security
Default wordt gebruik gemaakt van:
- username: user
- password generated
# Git plugin
To make it work on ubuntu use
```bash
export GIT_SSH=/usr/bin/ssh
```
otherwise an error occurs
```
[ERROR] Failed to perform fetch
Failed to perform fetch


org.eclipse.jgit.api.errors.TransportException: git@github.com:bvpelt/mybatch.git: USERAUTH fail
    at org.eclipse.jgit.api.FetchCommand.call (FetchCommand.java:254)
    at pl.project13.maven.git.JGitProvider.fetch (JGitProvider.java:352)
    at pl.project13.maven.git.JGitProvider.getAheadBehind (JGitProvider.java:339)
    at pl.project13.maven.git.GitDataProvider.lambda$loadGitData$17 (GitDataProvider.java:174)
```
# Using JobLauncher
The application has a buildin general interface to launch a job, the joblauncher.
Syntax: http://localhost:8080/joblauncher?job=<name>&(<paramname>=<paramvalue)*

Example:
http://localhost:8080/joblauncher?job=fileToPostgresLimitJob&fileName=BeschikkingF01.csv

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
- asciidoc
    - http://asciidoc.org/ 
    - cheatsheet https://powerman.name/doc/asciidoc
    - examples https://github.com/asciidoctor/asciidoctor-maven-examples
- asciidoc to dockbook 
    - http://docbkx-tools.sourceforge.net/docbkx-samples/manual.html
    - https://github.com/mimil/docbkx-tools

# Lombok
See 
- project https://projectlombok.org/
- intellij plugin https://projectlombok.org/setup/intellij
- https://www.baeldung.com/lombok-ide 

###
- https://stackoverflow.com/questions/6078009/how-to-get-access-to-job-parameters-from-itemreader-in-spring-batch

# Profiling
- https://www.jetbrains.com/help/idea/cpu-profiler.html

# Actuator
- https://www.callicoder.com/spring-boot-actuator/

see [grafana.md](grafana.md)

Customizing 
- https://docs.spring.io/spring-metrics/docs/current/public/prometheus
- https://blog.autsoft.hu/defining-custom-metrics-in-a-spring-boot-application-using-micrometer/
- http://micrometer.io/docs/concepts
- https://metrics.dropwizard.io/3.1.0/manual/servlets/

```bash
grep "15 Step after chunk - job"  /tmp/sample-boot-application.log | sed -e 's/.\{180\}//'
```



