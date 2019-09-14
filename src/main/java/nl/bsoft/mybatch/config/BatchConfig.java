package nl.bsoft.mybatch.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
@EnableTransactionManagement
public class BatchConfig extends DefaultBatchConfigurer {
    private static final Logger logger = LoggerFactory.getLogger(BatchConfig.class);

    @Autowired
    private JobBuilderFactory jobBuilder;

    @Autowired
    private StepBuilderFactory stepBuilder;

    private final DataSource dataSource;
    private final PlatformTransactionManager transactionManagerPg;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Autowired
    public BatchConfig(@Qualifier("dataSource") final DataSource dataSource,
                       @Qualifier("transactionManagerPg") final PlatformTransactionManager transactionManagerPg,
                       final JobBuilderFactory jobBuilderFactory,
                       final StepBuilderFactory stepBuilderFactory) {
        super(dataSource);
        logger.debug("Create BatchConfig - datasource: {}, transactionmanager: {}", dataSource.toString(), transactionManagerPg.toString());
        this.dataSource = dataSource;
        this.transactionManagerPg = transactionManagerPg;
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Override
    public PlatformTransactionManager getTransactionManager() {
        return this.transactionManagerPg;
    }

}
