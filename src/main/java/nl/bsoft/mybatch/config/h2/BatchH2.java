package nl.bsoft.mybatch.config.h2;

import nl.bsoft.mybatch.config.MyJobListener;
import nl.bsoft.mybatch.config.postgres.GegevensPgReader;
import nl.bsoft.mybatch.database.BeschikkingsBevoegdheid;
import nl.bsoft.mybatch.database.BeschikkingsBevoegdheidH2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BatchH2 {
    private static final Logger logger = LoggerFactory.getLogger(BatchH2.class);

    private final int DEFAULT_CHUNKSIZE = 10;
    private int chunkSize;

    @Autowired
    private JobBuilderFactory jobBuilder;

    @Autowired
    private StepBuilderFactory stepBuilder;

    public BatchH2() {
        this.chunkSize = DEFAULT_CHUNKSIZE;
    }

    public int getChunkSize() {
        return this.chunkSize;
    }

    public void setChunkSize(int chunkSize) {
        this.chunkSize = chunkSize;
    }

    @Bean
    public Job postgres2H2Job(final Step postgres2H2Step) {

        MyJobListener myJobListener = new MyJobListener();

        return jobBuilder.get("postgres2H2Job")
                .listener(myJobListener)
                .incrementer(new RunIdIncrementer())
                .start(postgres2H2Step)
                .build();
    }

    @Bean
    public Job file2H2Job(final Step fileToPostgresStepSkip,
                          final Step postgres2H2Step) {

        MyJobListener myJobListener = new MyJobListener();

        return jobBuilder.get("file2H2Job")
                .listener(myJobListener)
                .incrementer(new RunIdIncrementer())
                .start(fileToPostgresStepSkip)
                .next(postgres2H2Step)
                .build();
    }

    @Bean
    protected Step postgres2H2Step(GegevensPgReader<BeschikkingsBevoegdheid> reader,
                                   BeschikkingsBevoegdheidProcessor processor,
                                   BeschikkingsBevoegdheidH2Writer<BeschikkingsBevoegdheidH2> writer) {
        return stepBuilder.get("postgres2H2Step")
                .<BeschikkingsBevoegdheid, BeschikkingsBevoegdheidH2>chunk(chunkSize)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }
}
