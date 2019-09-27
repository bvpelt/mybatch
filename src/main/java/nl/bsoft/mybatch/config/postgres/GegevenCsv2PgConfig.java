package nl.bsoft.mybatch.config.postgres;

import nl.bsoft.mybatch.csv.Gegeven;
import nl.bsoft.mybatch.database.BeschikkingsBevoegdheid;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;
import java.util.Map;

@Configuration
public class GegevenCsv2PgConfig {
    private static final Logger logger = LoggerFactory.getLogger(GegevenCsv2PgConfig.class);

    private JobExecution jobExecution;
    private String fileName;
    private Date startDate;

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        this.jobExecution = stepExecution.getJobExecution();
        JobParameters jobParameters = jobExecution.getJobParameters();
        this.fileName = jobParameters.getString("filename");
        this.startDate = jobParameters.getDate("startdate");
        logger.error("Received parameters - filename: {}, startdate: {}", this.fileName, this.startDate.toString());
    }

    @Bean(name = "gegevensReader")
    public ItemReader<Gegeven> gegevensReader() {
        ItemReader<Gegeven> gegevenItemReader = new GegevensCsvReader();
        return gegevenItemReader;
    }

    @Bean(name = "gegevensWriter")
    public ItemWriter<BeschikkingsBevoegdheid> getGegevensWriter(@Qualifier("sfPostgres") SessionFactory sessionFactory) {
        GegevensPgWriter gegevensPgWriter = new GegevensPgWriter(sessionFactory);

        return gegevensPgWriter;
    }

    @Bean(name = "gegevensProcessor")
    public ItemProcessor<Gegeven, BeschikkingsBevoegdheid> getGegevensProcessor() {

        return new GegevensProcessor();
    }
}
