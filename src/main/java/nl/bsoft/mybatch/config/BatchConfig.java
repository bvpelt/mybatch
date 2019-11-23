package nl.bsoft.mybatch.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.JobExplorerFactoryBean;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Slf4j
@Configuration
@EnableBatchProcessing
@EnableTransactionManagement
public @Data
class BatchConfig extends DefaultBatchConfigurer {

    private final DataSource dataSourcePg;
    private final PlatformTransactionManager transactionManagerPg;

    @Autowired
    public BatchConfig(@Qualifier("dataSourcePg") final DataSource dataSourcePg,
                       final PlatformTransactionManager transactionManagerPg) {
        super(dataSourcePg);
        log.debug("Create BatchConfig - datasource: {}, transactionmanager: {}", dataSourcePg.toString(), transactionManagerPg.toString());
        this.dataSourcePg = dataSourcePg;
        this.transactionManagerPg = transactionManagerPg;
    }

    @Override
    public PlatformTransactionManager getTransactionManager() {
        log.debug("Get transaction manager");
        return this.transactionManagerPg;
    }

    @Override
    protected JobRepository createJobRepository() throws Exception {
        JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
        factory.setDataSource(this.dataSourcePg);
        factory.setTransactionManager(this.transactionManagerPg);
        factory.setIsolationLevelForCreate("ISOLATION_SERIALIZABLE");
        factory.setTablePrefix("BATCH_");
        factory.setMaxVarCharLength(2500);
        return factory.getObject();
    }

    @Override
    public JobExplorer getJobExplorer() {
        JobExplorerFactoryBean factory = new JobExplorerFactoryBean();
        factory.setDataSource(this.dataSourcePg);
        factory.setTablePrefix("BATCH_");
        JobExplorer jobExplorer = null;
        try {
            jobExplorer = factory.getObject();
        } catch (Exception e) {
            log.error("Couldnot find job explorer");
        }
        return jobExplorer;
    }
}
