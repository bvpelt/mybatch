package nl.bsoft.mybatch.config.h2;

import io.micrometer.prometheus.PrometheusMeterRegistry;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import nl.bsoft.mybatch.config.MyJobListener;
import nl.bsoft.mybatch.config.postgres.GegevensPgReader;
import nl.bsoft.mybatch.database.BeschikkingsBevoegdheid;
import nl.bsoft.mybatch.database.BeschikkingsBevoegdheidH2;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.HibernateItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public @Data
class BatchH2 {
    private final int DEFAULT_CHUNKSIZE = 10;
    private int chunkSize;
    private PrometheusMeterRegistry prometheusRegistry;

    @Autowired
    private JobBuilderFactory jobBuilder;

    @Autowired
    private StepBuilderFactory stepBuilder;

    @Autowired
    public BatchH2(PrometheusMeterRegistry prometheusRegistry) {
        this.chunkSize = DEFAULT_CHUNKSIZE;
        this.prometheusRegistry = prometheusRegistry;
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
                                   HibernateItemWriter<BeschikkingsBevoegdheidH2> writer) {
        return stepBuilder.get("postgres2H2Step")
                .<BeschikkingsBevoegdheid, BeschikkingsBevoegdheidH2>chunk(chunkSize)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }
}
