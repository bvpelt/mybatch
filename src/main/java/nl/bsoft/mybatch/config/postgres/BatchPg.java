package nl.bsoft.mybatch.config.postgres;

import nl.bsoft.mybatch.config.MyJobListener;
import nl.bsoft.mybatch.csv.Gegeven;
import nl.bsoft.mybatch.database.BeschikkingsBevoegdheid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BatchPg {
    private static final Logger logger = LoggerFactory.getLogger(BatchPg.class);

    private final int DEFAULT_CHUNKSIZE = 10;
    private int chunkSize;

    @Autowired
    private JobBuilderFactory jobBuilder;

    @Autowired
    private StepBuilderFactory stepBuilder;

    public BatchPg(final JobBuilderFactory jobBuilderFactory,
                   final StepBuilderFactory stepBuilderFactory) {
        this.jobBuilder = jobBuilderFactory;
        this.stepBuilder = stepBuilderFactory;
        this.chunkSize = DEFAULT_CHUNKSIZE;
    }

    public int getChunkSize() {
        return this.chunkSize;
    }

    public void setChunkSize(int chunkSize) {
        this.chunkSize = chunkSize;
    }

    @Bean(name = "fileToPostgresJob")
    public Job fileToPostgresJob(@Qualifier("fileToPostgresStep") Step fileToPostgresStep) {

        MyJobListener myJobListener = new MyJobListener();

        return jobBuilder.get("fileToPostgresJob")
                .listener(myJobListener)
                .incrementer(new RunIdIncrementer())
                .start(fileToPostgresStep)
                .build();
    }

    @Bean("fileToPostgresStep")
//    @StepScope
    protected Step fileToPostgresStep(@Qualifier("gegevensReader") ItemReader<Gegeven> reader,
                                      @Qualifier("gegevensProcessor") ItemProcessor<Gegeven, BeschikkingsBevoegdheid> processor,
                                      @Qualifier("gegevensWriter") ItemWriter<BeschikkingsBevoegdheid> writer) {
        return stepBuilder.get("fileToPostgresStep")
                .<Gegeven, BeschikkingsBevoegdheid>chunk(chunkSize)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }


}
