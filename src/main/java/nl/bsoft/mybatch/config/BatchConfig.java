package nl.bsoft.mybatch.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.JobExplorerFactoryBean;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
@EnableTransactionManagement
public class BatchConfig extends DefaultBatchConfigurer {
    private static final Logger logger = LoggerFactory.getLogger(BatchConfig.class);

    private final DataSource dataSourcePg;
    private final PlatformTransactionManager transactionManagerPg;

    @Autowired
    public BatchConfig(final DataSource dataSourcePg,
                       final PlatformTransactionManager transactionManagerPg) {
        super(dataSourcePg);
        logger.debug("Create BatchConfig - datasource: {}, transactionmanager: {}", dataSourcePg.toString(), transactionManagerPg.toString());
        this.dataSourcePg = dataSourcePg;
        this.transactionManagerPg = transactionManagerPg;
    }

    @Override
    public PlatformTransactionManager getTransactionManager() {
        logger.debug("Get transaction manager");
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
        JobExplorerFactoryBean factoryBean = new JobExplorerFactoryBean();
        factoryBean.setDataSource(this.dataSourcePg);
        JobExplorer jobExplorer = null;
        try {
            jobExplorer = factoryBean.getObject();
        } catch (Exception e) {
            logger.error("Couldnot find job explorer");
        }
        return jobExplorer;
    }
}
