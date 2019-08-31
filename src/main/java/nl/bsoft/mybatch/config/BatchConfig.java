package nl.bsoft.mybatch.config;

import nl.bsoft.mybatch.csv.Gegeven;
import nl.bsoft.mybatch.database.BeschikkingsBevoegdheid;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
@EnableTransactionManagement
@Import(DatabaseConfigPostgres.class)
public class BatchConfig extends DefaultBatchConfigurer {
    private static final Logger logger = LoggerFactory.getLogger(BatchConfig.class);

    @Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private StepBuilderFactory steps;

    private DataSource dataSource;

    private SessionFactory sessionFactory;

    private PlatformTransactionManager transactionManager;


    @Autowired
    public BatchConfig(@Qualifier("postgres") DataSource dataSource,
                       @Qualifier("sfPostgres") SessionFactory sessionFactory,
                       @Qualifier("transactionManagerPg") PlatformTransactionManager transactionManager) {
        this.dataSource = dataSource;
        this.sessionFactory = sessionFactory;
        this.transactionManager = transactionManager;
    }

    @Bean
    public Job job(@Qualifier("step1") Step step1) {

        return jobs.get("myJob")
                .start(step1)
                .build();
    }

    @Bean("step1")
    protected Step step1(@Qualifier("gegevensReader") ItemReader<Gegeven> reader,
                         @Qualifier("gegevensProcessor") ItemProcessor<Gegeven, BeschikkingsBevoegdheid> processor,
                         @Qualifier("gegevensWriter") ItemWriter<BeschikkingsBevoegdheid> writer) {
        return steps.get("step1")
                .<Gegeven, BeschikkingsBevoegdheid>chunk(10)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

}
