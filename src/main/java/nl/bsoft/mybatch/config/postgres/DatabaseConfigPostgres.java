package nl.bsoft.mybatch.config.postgres;

import com.zaxxer.hikari.HikariDataSource;
import liquibase.integration.spring.SpringLiquibase;
import lombok.extern.slf4j.Slf4j;
import nl.bsoft.mybatch.config.DatabaseConfig;
import org.hibernate.SessionFactory;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Properties;

@Slf4j
@Configuration
@EnableJpaRepositories(
        entityManagerFactoryRef = "entityManagerFactoryPg",
        transactionManagerRef = "transactionManagerPg"
)
public class DatabaseConfigPostgres extends DatabaseConfig {
    /*
        @Bean
        @Primary
        @ConfigurationProperties(prefix = "spring.datasource.postgres")
        public DataSource dataSourcePg() {
            log.debug("Get primary datasource");
            return DataSourceBuilder.create().build();
        }
    */
    @Bean
    @ConfigurationProperties("spring.datasource.postgres")
    public DataSourceProperties pgDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Primary
    @Bean(name = "dataSourcePg")
    @ConfigurationProperties("spring.datasource.postgres.configuration")
    public HikariDataSource dataSourcePg() {
        log.info("datasourcepg config: {}", pgDataSourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class).build().toString());
        return pgDataSourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Bean
    @ConfigurationProperties(prefix = "datasource.postgres.liquibase")
    public LiquibaseProperties liquibaseProperties() {
        return new LiquibaseProperties();
    }

    @Bean
    @Primary
    public SpringLiquibase liquibase(final DataSource dataSourcePg) {
        return springLiquibase(dataSourcePg, liquibaseProperties());
    }

    @Bean
    @Primary
    public PlatformTransactionManager transactionManagerPg() {
        log.debug("Get primary transactionManager");
        return new JpaTransactionManager(entityManagerFactoryPg().getObject());
    }

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryPg() {

        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();

        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(dataSourcePg());
        factoryBean.setJpaVendorAdapter(jpaVendorAdapter);
        factoryBean.setJpaProperties(hibernateProperties());
        factoryBean.setPackagesToScan("nl.bsoft.mybatch.config.postgres.repo", "nl.bsoft.mybatch.database");
        factoryBean.setPersistenceUnitName("postgres");

        return factoryBean;
    }

    @Bean
    public SessionFactory sfPostgres() throws SQLException {
        return entityManagerFactoryPg().getObject().unwrap(SessionFactory.class);
    }

    private Properties hibernateProperties() {
        final Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", "none");
        hibernateProperties.setProperty("hibernate.current_session_context_class", "thread");
        hibernateProperties.setProperty("hibernate.show_sql", "true");
        hibernateProperties.setProperty("hibernate.generate_statistics", "true");
        return hibernateProperties;
    }

}
