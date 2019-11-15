package nl.bsoft.mybatch.config.postgres;

import io.micrometer.prometheus.PrometheusMeterRegistry;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import nl.bsoft.mybatch.config.MyJobListener;
import nl.bsoft.mybatch.csv.Gegeven;
import nl.bsoft.mybatch.database.BeschikkingsBevoegdheid;
import nl.bsoft.mybatch.listeners.FileSkipListener;
import nl.bsoft.mybatch.utils.ExceptionLimitSkipPolicy;
import nl.bsoft.mybatch.utils.ExceptionSkipPolicy;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepListener;
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
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public @Data
class BatchPg {
    private static final String WILL_BE_INJECTED = null;
    private final int DEFAULT_CHUNKSIZE = 10;
    private final int NUMBER_FILE_EXCEPTIONS_TO_SKIP = 2;
    private final int DEFAULT_TIMEOUT = 2;

    private int chunkSize;

    @Autowired
    private JobBuilderFactory jobBuilder;

    @Autowired
    private StepBuilderFactory stepBuilder;

    @Autowired
    private PlatformTransactionManager transactionManagerPg;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private StepListener stepListener;

    private PrometheusMeterRegistry prometheusRegistry;

    @Autowired
    public BatchPg(PrometheusMeterRegistry prometheusRegistry) {
        this.chunkSize = DEFAULT_CHUNKSIZE;
        this.prometheusRegistry = prometheusRegistry;
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

    public TransactionAttribute transactionAttribute() {
        RuleBasedTransactionAttribute rbta = new RuleBasedTransactionAttribute();
        rbta.setIsolationLevel(TransactionDefinition.ISOLATION_DEFAULT);
        rbta.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        rbta.setTimeout(DEFAULT_TIMEOUT);

        return rbta;
    }

    public TransactionAttribute defTransAttr() {
        DefaultTransactionAttribute attribute = new DefaultTransactionAttribute();
        attribute.setPropagationBehavior(Propagation.REQUIRED.value());
        attribute.setIsolationLevel(Isolation.DEFAULT.value());

        return attribute;
    }

    @Bean
    @StepScope
    public FlatFileItemReader<Gegeven> csvItemReader(@Value("#{jobParameters['filename']}") final String fileName) throws IOException {
        log.debug("getItemReader with parameter filename: {}", fileName);

        Resource stateFile = new ClassPathResource(fileName);
        if (null == stateFile) {
            log.error("File: {} not found on classpath", fileName);
        }

        FlatFileItemReader<Gegeven> itemReader = new FlatFileItemReader<Gegeven>();
        itemReader.setLinesToSkip(1);
        itemReader.setResource(stateFile); //DelimitedLineTokenizer defaults to comma as its delimiter
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
        // MyStepListener<Gegeven, BeschikkingsBevoegdheid> ms = new MyStepListener<Gegeven, BeschikkingsBevoegdheid>();
        return stepBuilder.get("fileToPostgresStep")
                .<Gegeven, BeschikkingsBevoegdheid>chunk(chunkSize)
                .reader(csvItemReader(WILL_BE_INJECTED))
                .listener(stepListener)
                .faultTolerant()
                .skipLimit(2) // 2 parsing exceptions are ok
                .skip(FlatFileParseException.class)
                .processor(processor)
                .writer(gegevensWriter)
                .transactionManager(transactionManagerPg)
                .transactionAttribute(transactionAttribute())
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
        ItemReader<Gegeven> gegevenItemReader = new GegevensCsvReader(this.prometheusRegistry);
        return gegevenItemReader;
    }

    @Bean
    public ItemProcessor<Gegeven, BeschikkingsBevoegdheid> gegevensProcessor(PrometheusMeterRegistry prometheusRegistry) {
        return new GegevensProcessor(prometheusRegistry);
    }

    @Bean
    public ExceptionSkipPolicy fileException() {
        return new ExceptionSkipPolicy(FlatFileParseException.class);
    }

    @Bean
    public ExceptionLimitSkipPolicy fileExceptionSet() {

        Map<Class<? extends Throwable>, Boolean> skippableExceptions = new HashMap<Class<? extends Throwable>, Boolean>();
        skippableExceptions.put(FlatFileParseException.class, true);
        return new ExceptionLimitSkipPolicy(NUMBER_FILE_EXCEPTIONS_TO_SKIP, skippableExceptions);
    }

    @Bean
    public FileSkipListener fileSkipListener() {
        return new FileSkipListener();
    }
}
