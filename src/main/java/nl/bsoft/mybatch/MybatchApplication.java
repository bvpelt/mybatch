package nl.bsoft.mybatch;

import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import lombok.extern.slf4j.Slf4j;
import nl.bsoft.mybatch.services.PrometheusService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.objenesis.SpringObjenesis;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.core.parameters.P;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
        (exclude = {
                DataSourceAutoConfiguration.class,
                HibernateJpaAutoConfiguration.class,
                LiquibaseAutoConfiguration.class,
                BatchAutoConfiguration.class})
//@EnableSwagger2
@Slf4j
@EnableAsync
@EnableTransactionManagement
public class MybatchApplication extends SpringBootServletInitializer {

    private static final Class<MybatchApplication> APP_CLASS = MybatchApplication.class;


    private static PrometheusService prometheusService = new PrometheusService();

    public static void main(String[] args) {
        log.info("Bezig met configureren...van");

        /*
        try {
            prometheusService.startUp();
        } catch (Exception e) {
            log.error("Error starting prometheus");
        }
         */
        SpringApplication.run(APP_CLASS, args);
    }

    @Override
    protected SpringApplicationBuilder configure(final SpringApplicationBuilder application) {
        return application.sources(APP_CLASS);
    }
    
}
