package nl.bsoft.mybatch.config.h2;

import com.zaxxer.hikari.HikariDataSource;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.integration.spring.SpringLiquibase;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.extern.slf4j.Slf4j;
import nl.bsoft.mybatch.config.DatabaseConfig;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseDataSource;
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
    @ConfigurationProperties("h2.datasource")
    public DataSourceProperties h2DataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties("h2.datasource.configuration")
    public HikariDataSource dataSourceH2() {
        log.info("datasourceh2 config: {}", h2DataSourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class).build().toString());
        return h2DataSourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    /*
    @LiquibaseDataSource
    public DataSource liqDataSourceH2(@Qualifier("dataSourceH2") final HikariDataSource dataSourceH2) {
        // Create data source, set pool size to 1 and return it
        return (DataSource) dataSourceH2;
    }

     */

    @PostConstruct
    public void setUpHikariWithMetrics() {
        if (dataSourceH2() instanceof HikariDataSource) {
            ((HikariDataSource) dataSourceH2()).setMetricRegistry(prometheusMeterRegistry);
        }
    }

    @Bean
    public SessionFactory sfH2(@Qualifier("entityManagerFactoryH2") final LocalContainerEntityManagerFactoryBean entityManagerFactoryH2) {
        return Objects.requireNonNull(entityManagerFactoryH2.getObject()).unwrap(SessionFactory.class);
    }

    @Bean
    @ConfigurationProperties(prefix = "datasource.h2.h2liquibase")
    public LiquibaseProperties liquibasePropertiesH2() {
        return new LiquibaseProperties();
    }

    @Bean
    @DependsOn("dataSourceH2")
    public SpringLiquibase liquibaseH2(@Qualifier("dataSourceH2") final DataSource dataSource,
                                       @Qualifier("liquibasePropertiesH2") LiquibaseProperties liquibasePropertiesH2) {
        return springLiquibase(dataSource, liquibasePropertiesH2);
    }

/*
    @Bean
    public Liquibase lqH2(@Qualifier("dataSourceH2") final DataSource dataSource) throws SQLException, LiquibaseException {

        Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(dataSource.getConnection()));

        Liquibase liquibase = new Liquibase("db/changelog/changelog-h2-master.yaml", new ClassLoaderResourceAccessor(), database);

        liquibase.update(new Contexts(), new LabelExpression());

        return liquibase;
    }
*/

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryH2(@Qualifier("dataSourceH2") final DataSource dataSourceH2) throws SQLException {
        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(dataSourceH2);
        factoryBean.setJpaVendorAdapter(jpaVendorAdapter);
        factoryBean.setJpaProperties(hibernateProperties());
        factoryBean.setPackagesToScan("nl.bsoft.mybatch.config.h2.repo", "nl.bsoft.mybatch.database");
        factoryBean.setPersistenceUnitName("h2-unit");

        return factoryBean;
    }

    @Bean
    public PlatformTransactionManager transactionManagerH2(@Qualifier("entityManagerFactoryH2") LocalContainerEntityManagerFactoryBean entityManagerFactoryH2) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactoryH2.getObject());
        return transactionManager;
    }

    private Properties hibernateProperties() {
        final Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        return hibernateProperties;
    }

}
