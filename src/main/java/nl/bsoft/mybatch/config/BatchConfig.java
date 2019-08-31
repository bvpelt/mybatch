package nl.bsoft.mybatch.config;

import nl.bsoft.mybatch.csv.Gegeven;
import nl.bsoft.mybatch.database.BeschikkingsBevoegdheid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

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

    @Autowired
    public BatchConfig() {
        chunkSize = DEFAULT_CHUNKSIZE;
    }

    public int getChunkSize() {
        return chunkSize;
    }

    public void setChunkSize(int chunkSize) {
        this.chunkSize = chunkSize;
    }

    @Bean
    public Job job(@Qualifier("step1") Step step1) {

        MyJobListener myJobListener = new MyJobListener();

        return jobBuilder.get("myJob")
                .listener(myJobListener)
                .incrementer(new RunIdIncrementer())
                .start(step1)
                .build();
    }

    @Bean("step1")
    protected Step step1(@Qualifier("gegevensReader") ItemReader<Gegeven> reader,
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
