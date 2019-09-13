package nl.bsoft.mybatch.config;

import nl.bsoft.mybatch.csv.Gegeven;
import nl.bsoft.mybatch.database.BeschikkingsBevoegdheid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
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

    private final int DEFAULT_CHUNKSIZE = 10;
    private int chunkSize;

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
        this.chunkSize = DEFAULT_CHUNKSIZE;
    }

    public int getChunkSize() {
        return this.chunkSize;
    }

    public void setChunkSize(int chunkSize) {
        this.chunkSize = chunkSize;
    }

    @Override
    public PlatformTransactionManager getTransactionManager() {
        return this.transactionManagerPg;
    }

    @Bean(name = "fileToPostgresJob")
    public Job fileToPostgresJob(@Qualifier("fileToPostgresStep") Step step1) {

        MyJobListener myJobListener = new MyJobListener();

        return jobBuilder.get("myJob")
                .listener(myJobListener)
                .incrementer(new RunIdIncrementer())
                .start(step1)
                .build();
    }

    @Bean("fileToPostgresStep")
    protected Step fileToPostgresStep(@Qualifier("gegevensReader") ItemReader<Gegeven> reader,
                                      @Qualifier("gegevensProcessor") ItemProcessor<Gegeven, BeschikkingsBevoegdheid> processor,
                                      @Qualifier("gegevensWriter") ItemWriter<BeschikkingsBevoegdheid> writer) {
        return stepBuilder.get("step1")
                .<Gegeven, BeschikkingsBevoegdheid>chunk(chunkSize)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean(name = "postgres2H2Job")
    public Job postgres2H2Job(@Qualifier("postgres2H2Step") Step step1) {

        MyJobListener myJobListener = new MyJobListener();

        return jobBuilder.get("myJob")
                .listener(myJobListener)
                .incrementer(new RunIdIncrementer())
                .start(step1)
                .build();
    }

    @Bean("postgres2H2Step")
    protected Step postgres2H2Step(@Qualifier("gegevensReader") ItemReader<Gegeven> reader,
                                   @Qualifier("gegevensProcessor") ItemProcessor<Gegeven, BeschikkingsBevoegdheid> processor,
                                   @Qualifier("gegevensWriter") ItemWriter<BeschikkingsBevoegdheid> writer) {
        return stepBuilder.get("step1")
                .<Gegeven, BeschikkingsBevoegdheid>chunk(chunkSize)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }
}
