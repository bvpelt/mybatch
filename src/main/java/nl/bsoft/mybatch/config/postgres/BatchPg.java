package nl.bsoft.mybatch.config.postgres;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import nl.bsoft.mybatch.config.MyJobListener;
import nl.bsoft.mybatch.csv.Gegeven;
import nl.bsoft.mybatch.database.BeschikkingsBevoegdheid;
import nl.bsoft.mybatch.listeners.FileSkipListener;
import nl.bsoft.mybatch.listeners.MyStepListener;
import nl.bsoft.mybatch.utils.ExceptionLimitSkipPolicy;
import nl.bsoft.mybatch.utils.ExceptionSkipPolicy;
import nl.bsoft.mybatch.utils.FileUtils;
import org.hibernate.SessionFactory;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.ResourceLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public @Data
class BatchPg {
    private static final String WILL_BE_INJECTED = null;
    private final int DEFAULT_CHUNKSIZE = 10;
    private int chunkSize;

    @Autowired
    private JobBuilderFactory jobBuilder;

    @Autowired
    private StepBuilderFactory stepBuilder;

    @Autowired
    private ResourceLoader resourceLoader;

    public BatchPg() {
        this.chunkSize = DEFAULT_CHUNKSIZE;
    }

    @Bean
    public Job fileToPostgresSkipJob(final Step fileToPostgresStepSkip) {

        MyJobListener myJobListener = new MyJobListener();

        return jobBuilder.get("fileToPostgresSkipJob")
                .listener(myJobListener)
                .incrementer(new RunIdIncrementer())
                .start(fileToPostgresStepSkip)
                .build();
    }

    @Bean
    public Job fileToPostgresLimitJob(final Step fileToPostgresStepLimit) {

        MyJobListener myJobListener = new MyJobListener();

        return jobBuilder.get("fileToPostgresLimitJob")
                .listener(myJobListener)
                .incrementer(new RunIdIncrementer())
                .start(fileToPostgresStepLimit)
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<Gegeven> csvItemReader(@Value("#{jobParameters['filename']}") final String fileName) throws IOException {
        log.debug("getItemReader with parameter filename: {}", fileName);

        File file = null;
        String fullFileName = null;

        file = FileUtils.search(fileName);
        if (file == null) {
            throw new FileNotFoundException("file: " + fileName);
        } else {
            fullFileName = file.toString();
        }

        FlatFileItemReader<Gegeven> itemReader = new FlatFileItemReader<Gegeven>();
        itemReader.setLinesToSkip(1);
        itemReader.setResource(new FileSystemResource(fullFileName)); //DelimitedLineTokenizer defaults to comma as its delimiter
        DefaultLineMapper<Gegeven> lineMapper = new DefaultLineMapper<Gegeven>();
        lineMapper.setLineTokenizer(new DelimitedLineTokenizer());
        lineMapper.setFieldSetMapper(new GegevensCsvFieldSetMapper());
        itemReader.setLineMapper(lineMapper);
        itemReader.open(new ExecutionContext());

        return itemReader;
    }

    @Bean
    public Step fileToPostgresStepSkip(ItemReader<Gegeven> csvItemReader,
                                       ItemProcessor<Gegeven, BeschikkingsBevoegdheid> processor,
                                       ItemWriter<BeschikkingsBevoegdheid> gegevensWriter) throws IOException {
        MyStepListener<Gegeven,BeschikkingsBevoegdheid> ms = new MyStepListener<Gegeven, BeschikkingsBevoegdheid>();
        return stepBuilder.get("fileToPostgresStep")
                .<Gegeven, BeschikkingsBevoegdheid>chunk(chunkSize)
                .reader(csvItemReader(WILL_BE_INJECTED))
                .listener(ms)
                .faultTolerant()
                .skipLimit(2) // 2 parsing exceptions are ok
                .skip(FlatFileParseException.class)
                .processor(processor)
                .writer(gegevensWriter)
                .build();
    }


    @Bean
    public Step fileToPostgresStepLimit(ItemReader<Gegeven> csvItemReader,
                                        ItemProcessor<Gegeven, BeschikkingsBevoegdheid> processor,
                                        ItemWriter<BeschikkingsBevoegdheid> gegevensWriter) throws IOException {
        return stepBuilder.get("fileToPostgresStep")
                .<Gegeven, BeschikkingsBevoegdheid>chunk(chunkSize)
                .reader(csvItemReader(WILL_BE_INJECTED))
                .faultTolerant()
                .skipPolicy(fileExceptionSet())
                .listener(fileSkipListener())
                .processor(processor)
                .writer(gegevensWriter)
//                .faultTolerant()
//                .skip(DataException.class)
//                .startLimit(2) // Only 2 starts for this step
//                .allowStartIfComplete(true) // Restart always possible
                .build();
    }


    @Bean
    public ItemReader<Gegeven> gegevensReader() {
        ItemReader<Gegeven> gegevenItemReader = new GegevensCsvReader();
        return gegevenItemReader;
    }

    @Bean
    public ItemWriter<BeschikkingsBevoegdheid> gegevensWriter(SessionFactory sfPostgres) {
        GegevensPgWriter gegevensPgWriter = new GegevensPgWriter(sfPostgres);

        return gegevensPgWriter;
    }

    @Bean
    public ItemProcessor<Gegeven, BeschikkingsBevoegdheid> gegevensProcessor() {

        return new GegevensProcessor();
    }

    @Bean
    public ExceptionSkipPolicy fileException() {
        return new ExceptionSkipPolicy(FlatFileParseException.class);
    }

    @Bean
    public ExceptionLimitSkipPolicy fileExceptionSet() {
        Map<Class<? extends Throwable>, Boolean> skippableExceptions = new HashMap<Class<? extends Throwable>, Boolean>();
        skippableExceptions.put(FlatFileParseException.class, true);
        return new ExceptionLimitSkipPolicy(2, skippableExceptions);
    }

    @Bean
    public FileSkipListener fileSkipListener() {
        return new FileSkipListener();
    }
}
