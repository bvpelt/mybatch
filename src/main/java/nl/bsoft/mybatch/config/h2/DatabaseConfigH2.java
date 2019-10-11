package nl.bsoft.mybatch.config.h2;

import liquibase.integration.spring.SpringLiquibase;
import lombok.extern.slf4j.Slf4j;
import nl.bsoft.mybatch.config.DatabaseConfig;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
        entityManagerFactoryRef = "entityManagerFactoryH2",
        transactionManagerRef = "transactionManagerH2"
)
public class DatabaseConfigH2 extends DatabaseConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.h2")
    public DataSource dataSourceH2() {

        return DataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties(prefix = "datasource.h2.h2liquibase")
    public LiquibaseProperties liquibasePropertiesH2() {

        return new LiquibaseProperties();
    }

    @Bean
    public SpringLiquibase liquibaseH2(@Qualifier("dataSourceH2") final DataSource dataSource) {
        return springLiquibase(dataSource, liquibasePropertiesH2());
    }

    @Bean
    public PlatformTransactionManager transactionManagerH2() {
        return new JpaTransactionManager(entityManagerFactoryH2().getObject());
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryH2() {

        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();

        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(dataSourceH2());
        factoryBean.setJpaVendorAdapter(jpaVendorAdapter);
        factoryBean.setJpaProperties(hibernateProperties());
        factoryBean.setPackagesToScan("nl.bsoft.mybatch.config.h2.repo", "nl.bsoft.mybatch.database");
        factoryBean.setPersistenceUnitName("h2");

        return factoryBean;
    }

    @Bean(destroyMethod = "")
    public SessionFactory sessionFactoryH2() throws SQLException {
        return entityManagerFactoryH2().getObject().unwrap(SessionFactory.class);
    }

    private Properties hibernateProperties() {
        final Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", "none");
        hibernateProperties.setProperty("hibernate.current_session_context_class", "thread");
        hibernateProperties.setProperty("hibernate.show_sql", "true");
        hibernateProperties.setProperty("hibernate.generate_statistics", "true");
        return hibernateProperties;
    }

}
