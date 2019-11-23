package nl.bsoft.mybatch.config.h2;

import com.zaxxer.hikari.HikariDataSource;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import liquibase.integration.spring.SpringLiquibase;
import lombok.extern.slf4j.Slf4j;
import nl.bsoft.mybatch.config.DatabaseConfig;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Properties;

@Slf4j
@Configuration
@EnableJpaRepositories(
        entityManagerFactoryRef = "entityManagerFactoryH2",
        transactionManagerRef = "transactionManagerH2"
)
public class DatabaseConfigH2 extends DatabaseConfig {

    private PrometheusMeterRegistry prometheusMeterRegistry;

    public DatabaseConfigH2(final PrometheusMeterRegistry prometheusMeterRegistry) {
        this.prometheusMeterRegistry = prometheusMeterRegistry;
    }

    @Bean
    @ConfigurationProperties("h2.datasource")
    public DataSourceProperties h2DataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "dataSourceH2")
    @ConfigurationProperties("h2.datasource.configuration")
    public HikariDataSource dataSourceH2() {
        log.info("datasourceh2 config: {}", h2DataSourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class).build().toString());
        return h2DataSourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @PostConstruct
    public void setUpHikariWithMetrics() {
        if (dataSourceH2() instanceof HikariDataSource) {
            ((HikariDataSource) dataSourceH2()).setMetricRegistry(prometheusMeterRegistry);
        }
    }

    @Bean(name = "sfH2")
    public SessionFactory sessionFactoryH2() throws SQLException {
        return entityManagerFactoryH2().getObject().unwrap(SessionFactory.class);
    }

    @Bean("liquibasePropertiesH2")
    @ConfigurationProperties(prefix = "datasource.h2.h2liquibase")
    public LiquibaseProperties liquibasePropertiesH2() {

        return new LiquibaseProperties();
    }

    @Bean("liquibaseH2")
    @DependsOn("dataSourceH2")
    public SpringLiquibase liquibaseH2(@Qualifier("dataSourceH2") final DataSource dataSource) {
        return springLiquibase(dataSource, liquibasePropertiesH2());
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryH2() throws SQLException {

        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();

        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(dataSourceH2());
        factoryBean.setJpaVendorAdapter(jpaVendorAdapter);
        factoryBean.setJpaProperties(hibernateProperties());
        factoryBean.setPackagesToScan("nl.bsoft.mybatch.config.h2.repo", "nl.bsoft.mybatch.database");
        factoryBean.setPersistenceUnitName("h2-unit");

        return factoryBean;
    }

    @Bean
    public PlatformTransactionManager transactionManagerH2() throws SQLException {
        return new JpaTransactionManager(entityManagerFactoryH2().getObject());
    }

    private Properties hibernateProperties() {
        final Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
/*
        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", "none");
        hibernateProperties.setProperty("hibernate.current_session_context_class", "thread");
        hibernateProperties.setProperty("hibernate.show_sql", "true");
        hibernateProperties.setProperty("hibernate.generate_statistics", "true");

 */
        return hibernateProperties;
    }

}
