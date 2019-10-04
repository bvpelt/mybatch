package nl.bsoft.mybatch.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Slf4j
public class MyJobListener implements JobExecutionListener {

    @Override
    public void beforeJob(JobExecution jobExecution) {
        long jobId = jobExecution.getJobId();
        Date input = jobExecution.getStartTime();
        LocalDateTime date = input.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm:ss");

        log.info("Staring job: {} at: {} - {}", jobId, date.format(dateTimeFormatter), jobExecution.toString());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        long jobId = jobExecution.getJobId();
        BatchStatus jobStatus = jobExecution.getStatus();

        log.info("Ended job: {} with status: {} - ", jobId, jobStatus.name(), jobExecution.toString());
    }
}
