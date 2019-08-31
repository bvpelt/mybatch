package nl.bsoft.mybatch.config;

import liquibase.integration.spring.SpringLiquibase;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Properties;

@Configuration
@EnableJpaRepositories(
        entityManagerFactoryRef = "entityManagerFactoryPg",
        transactionManagerRef = "transactionManagerPg",
        basePackages = {"nl.bsoft.mybatch.database"}
)
public class DatabaseConfigPostgres {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConfigPostgres.class);

    @Bean(name = "postgres")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.postgres")
    public DataSource dataSource() {

        return DataSourceBuilder.create().build();
    }

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "datasource.postgres.liquibase")
    public LiquibaseProperties liquibaseProperties() {

        return new LiquibaseProperties();
    }

    @Bean("liquibase")
    public SpringLiquibase liquibase(@Qualifier("postgres") final DataSource dataSource) {
        return springLiquibase(dataSource, liquibaseProperties());
    }

    private SpringLiquibase springLiquibase(DataSource dataSource, LiquibaseProperties properties) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource);
        liquibase.setChangeLog(properties.getChangeLog());
        liquibase.setContexts(properties.getContexts());
        liquibase.setDefaultSchema(properties.getDefaultSchema());
        liquibase.setDropFirst(properties.isDropFirst());
        liquibase.setShouldRun(properties.isEnabled());
        liquibase.setLabels(properties.getLabels());
        liquibase.setChangeLogParameters(properties.getParameters());
        liquibase.setRollbackFile(properties.getRollbackFile());
        return liquibase;
    }

    @Bean(name = "transactionManagerPg")
    @Primary
    public PlatformTransactionManager transactionManagerPg() {
        return new JpaTransactionManager(entityManagerFactoryPg().getObject());
    }

    @Bean(name = "entityManagerFactoryPg")
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryPg() {

        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();

        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(dataSource());
        factoryBean.setJpaVendorAdapter(jpaVendorAdapter);
        factoryBean.setJpaProperties(hibernateProperties());

        factoryBean.setPackagesToScan("nl.bsoft.mybatch.database");
        factoryBean.setPersistenceUnitName("postgres");

        return factoryBean;
    }

    @Bean(name = "sfPostgres", destroyMethod = "")
    public SessionFactory getSessionFactory() throws SQLException {
        return entityManagerFactoryPg().getObject().unwrap(SessionFactory.class);
    }

    private Properties hibernateProperties() {
        final Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", "none");

        hibernateProperties.setProperty("hibernate.current_session_context_class", "thread");
        //hibernateProperties.setProperty("hibernate.transaction.coordinator_class", "jdbc");
        hibernateProperties.setProperty("hibernate.show_sql", "true");
        hibernateProperties.setProperty("hibernate.generate_statistics", "true");
        return hibernateProperties;
    }

}
