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
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.Objects;
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
    @ConfigurationProperties("spring.datasource.h2")
    public DataSourceProperties h2DataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "dataSourceH2")
    @ConfigurationProperties("spring.datasource.h2.configuration")
    public DataSource dataSourceH2(final DataSourceProperties h2DataSourceProperties) {
        log.info("datasourceh2 config: {}", h2DataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build().toString());
        return h2DataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Bean
    public SessionFactory sessionFactoryH2(@Qualifier("entityManagerFactoryH2") final LocalContainerEntityManagerFactoryBean entityManagerFactoryH2) {
        return Objects.requireNonNull(entityManagerFactoryH2.getObject()).unwrap(SessionFactory.class);
    }

    @PostConstruct
    public void setUpHikariWithMetrics() {
        DataSource dataSource = dataSourceH2(h2DataSourceProperties());
        if (dataSource instanceof HikariDataSource) {
            ((HikariDataSource) dataSource).setMetricRegistry(prometheusMeterRegistry);
        }
    }

    @Bean
    @ConfigurationProperties(prefix = "datasource.h2.h2liquibase")
    public LiquibaseProperties liquibasePropertiesH2() {
        return new LiquibaseProperties();
    }

    @Bean
    public SpringLiquibase liquibaseH2(@Qualifier("dataSourceH2") final DataSource dataSourceH2) {
        return springLiquibase(dataSourceH2, liquibasePropertiesH2());
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryH2(@Qualifier("dataSourceH2") final DataSource dataSourceH2) {

        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();

        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(dataSourceH2);
        factoryBean.setJpaVendorAdapter(jpaVendorAdapter);
        factoryBean.setJpaProperties(hibernateProperties());
        factoryBean.setPackagesToScan("nl.bsoft.mybatch.config.h2.repo", "nl.bsoft.mybatch.database");
        factoryBean.setPersistenceUnitName("h2");

        return factoryBean;
    }

    @Bean
    public PlatformTransactionManager transactionManagerH2(@Qualifier("dataSourceH2") final DataSource dataSourceH2) {
        log.debug("Get h2 transactionManager");
        return new JpaTransactionManager(entityManagerFactoryH2(dataSourceH2).getObject());
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
