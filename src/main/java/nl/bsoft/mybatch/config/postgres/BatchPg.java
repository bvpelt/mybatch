package nl.bsoft.mybatch.config.postgres;

import nl.bsoft.mybatch.config.MyJobListener;
import nl.bsoft.mybatch.csv.Gegeven;
import nl.bsoft.mybatch.database.BeschikkingsBevoegdheid;
import org.hibernate.exception.DataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Configuration
public class BatchPg {
    private static final Logger logger = LoggerFactory.getLogger(BatchPg.class);
    private static final String WILL_BE_INJECTED = null;
    private final int DEFAULT_CHUNKSIZE = 10;
    private int chunkSize;

    @Autowired
    private JobBuilderFactory jobBuilder;

    @Autowired
    private StepBuilderFactory stepBuilder;

    public BatchPg() {
        this.chunkSize = DEFAULT_CHUNKSIZE;
    }

    public int getChunkSize() {
        return this.chunkSize;
    }

    public void setChunkSize(final int chunkSize) {
        this.chunkSize = chunkSize;
    }

    @Bean
    public Job fileToPostgresJob(final Step fileToPostgresStep) {

        MyJobListener myJobListener = new MyJobListener();

        return jobBuilder.get("fileToPostgresJob")
                .listener(myJobListener)
                .incrementer(new RunIdIncrementer())
                .start(fileToPostgresStep)
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<Gegeven> csvItemReader(@Value("#{jobParameters['filename']}") final String fileName) {
        logger.debug("getItemReader with parameter filename: {}", fileName);
        FlatFileItemReader<Gegeven> itemReader = new FlatFileItemReader<Gegeven>();
        itemReader.setLinesToSkip(1);
        itemReader.setResource(new FileSystemResource("src/main/resources/" + fileName)); //DelimitedLineTokenizer defaults to comma as its delimiter
        DefaultLineMapper<Gegeven> lineMapper = new DefaultLineMapper<Gegeven>();
        lineMapper.setLineTokenizer(new DelimitedLineTokenizer());
        lineMapper.setFieldSetMapper(new GegevensCsvFieldSetMapper());
        itemReader.setLineMapper(lineMapper);
        itemReader.open(new ExecutionContext());

        return itemReader;
    }

    @Bean
    public Step fileToPostgresStep(ItemReader<Gegeven> csvItemReader,
                                   @Qualifier("gegevensProcessor") ItemProcessor<Gegeven, BeschikkingsBevoegdheid> processor,
                                   @Qualifier("gegevensWriter") ItemWriter<BeschikkingsBevoegdheid> writer) {
        return stepBuilder.get("fileToPostgresStep")
                .<Gegeven, BeschikkingsBevoegdheid>chunk(chunkSize)
                .reader(csvItemReader(WILL_BE_INJECTED))
                .faultTolerant()
                .skipLimit(2) // 2 parsing exceptions are ok
                .skip(FlatFileParseException.class)
                .processor(processor)
                .writer(writer)
                .faultTolerant()
                .skip(DataException.class)
                .startLimit(2) // Only 2 starts for this step
                .allowStartIfComplete(true) // Restart always possible
                .build();
    }

}
