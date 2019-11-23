package nl.bsoft.mybatch.listeners;

import io.micrometer.core.instrument.Counter;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import nl.bsoft.mybatch.config.StepListenerConfig;
import org.springframework.batch.core.*;
import org.springframework.batch.core.annotation.*;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * See https://docs.spring.io/spring-batch/trunk/apidocs/org/springframework/batch/core/listener/StepListenerSupport.html
 * vor documentation of step listener interface
 * <p>
 * INPUT is the source type in a step definition
 * OUTPUT is the destination type in a step definition
 */

@Slf4j
@Component
public @Data
class MyStepListener<INPUT, OUTPUT> implements StepListener {

    private StepExecution stepExecution;

    private StepListenerConfig stepListenerConfig;

    private PrometheusMeterRegistry registry;

    private Counter writeCounter;
    private Counter readCounter;
    private Counter commitCounter;
    private Counter rollbackCounter;

    @Autowired
    public MyStepListener(final StepListenerConfig stepListenerConfig,
                          final PrometheusMeterRegistry registry) {
        this.stepListenerConfig = stepListenerConfig;
        this.registry = registry;
        writeCounter = Counter.builder("writer")
                .baseUnit("mybatch")
                .description("Counter for written records")
                .tags("aantal", "writer")
                .register(registry);
        readCounter = Counter.builder("writer")
                .baseUnit("mybatch")
                .description("Counter for written records")
                .tags("aantal", "reader")
                .register(registry);
        commitCounter = Counter.builder("writer")
                .baseUnit("mybatch")
                .description("Counter for written records")
                .tags("aantal", "commit")
                .register(registry);
        rollbackCounter = Counter.builder("writer")
                .baseUnit("mybatch")
                .description("Counter for written records")
                .tags("aantal", "rollback")
                .register(registry);
    }

    @BeforeStep
    public void beforeStep(final StepExecution stepExecution) {
        this.stepExecution = stepExecution;

        if (stepListenerConfig.isLogBeforeStep()) {
            log.debug("01 Step before: {} started at: {} ", stepExecution.getStepName(), stepExecution.getStartTime().toString());
            JobParameters jobParameters = this.stepExecution.getJobParameters();
            Map<String, JobParameter> params = jobParameters.getParameters();
            params.forEach((k, v) -> {
                log.debug("01 Step before - Parameter: {}, value: {}", k, v);
            });
        }
    }

    @BeforeChunk
    public void beforeChunk(final ChunkContext context) {
        if (stepListenerConfig.isLogBeforeChunk()) {
            log.debug("02 Step before chunck");
        }
    }

    @BeforeRead
    public void beforeRead() {
        if (stepListenerConfig.isLogBeforeRead()) {
            log.debug("03 Step before read");
        }
    }

    @AfterRead
    public void afterRead(final INPUT item) {
        if (stepListenerConfig.isLogAfterRead()) {
            log.debug("04 Step after read item: {} ", item.toString());
        }
        if (stopConditionsMet()) {
            this.stepExecution.setTerminateOnly();
        }
    }

    @OnReadError
    public void onReadError(final Exception ex) {
        if (stepListenerConfig.isLogReadError()) {
            log.debug("05 Step read error: {}", ex.getMessage());
        }
    }

    @OnSkipInRead
    public void onSkipInRead(final Throwable t) {
        if (stepListenerConfig.isLogSkipInRead()) {
            log.debug("06 Step after read skip on exception: {}", t.getMessage());
        }
    }

    @BeforeProcess
    public void beforeProcess(final INPUT item) {
        if (stepListenerConfig.isLogBeforeProcess()) {
            log.debug("07 Step before process of item: {}", item.toString());
        }
    }

    @AfterProcess
    public void afterProcess(
            final INPUT item, final OUTPUT result) {
        if (stepListenerConfig.isLogAfterProcess()) {
            log.debug("08 Step after process: {}", stepExecution.getStepName());
        }
    }

    @OnProcessError
    public void onProcessError(final INPUT item, Exception e) {
        if (stepListenerConfig.isLogProcessError()) {
            log.debug("09 Step after process error: {}", e.getMessage());
        }
    }

    @OnSkipInProcess
    public void onSkipInProcess(final INPUT item, final Throwable t) {
        if (stepListenerConfig.isLogSkipInProcess()) {
            log.debug("10 Step after skip in proces - item: {}, exception: {}", item.toString(), t.getMessage());
        }
    }

    @BeforeWrite
    public void beforeWrite(final List<? extends OUTPUT> items) {
        if (stepListenerConfig.isLogBeforeWrite()) {
            log.debug("11 Step before write of {} items", items.size());
            items.forEach((i) -> {
                log.debug("11 Step before write of item: {}", i.toString());
            });
        }
    }

    @AfterWrite
    public void afterWrite(final List<? extends OUTPUT> items) {
        if (stepListenerConfig.isLogAfterWrite()) {
            log.debug("12 Step after write of {} items", items.size());
            items.forEach((i) -> {
                log.debug("12 Step after write of item: {}", i.toString());
            });
        }
    }

    @OnWriteError
    public void onWriteError(final Exception exception, final List<? extends OUTPUT> items) {
        if (stepListenerConfig.isLogWriteError()) {
            log.debug("13 Step on write error of {} items with exception: {}", items.size(), exception.getMessage());
            items.forEach((i) -> {
                log.debug("13 Step on write error of item: {}", i.toString());
            });
        }
    }

    @OnSkipInWrite
    public void onSkipInWrite(final OUTPUT item, final Throwable t) {
        if (stepListenerConfig.isLogSkipInWrite()) {
            log.debug("14 Step skip in write on item: {}, exception: {}", item.toString(), t.getMessage());
        }
    }

    @AfterChunk
    public void afterChunk(final ChunkContext context) {
        if (stepListenerConfig.isLogAfterChunk()) {
            log.debug("15 Step after chunck, is complete: {}", context.isComplete());

            StepContext stepContext = context.getStepContext();
            StepExecution stepExecution = stepContext.getStepExecution();
            ExitStatus exitStatus = stepExecution.getExitStatus();
            BatchStatus batchStatus = stepExecution.getStatus();
            String stepName = stepExecution.getStepName();
            JobExecution jobExecution = stepExecution.getJobExecution();
            String jobName = jobExecution.getJobInstance().getJobName();
            int commitCount = stepExecution.getCommitCount();
            int rollbackCount = stepExecution.getRollbackCount();
            int writeCount = stepExecution.getWriteCount();
            int readCount = stepExecution.getReadCount();

            writeCounter.increment(writeCount);
            readCounter.increment(readCount);
            commitCounter.increment(commitCount);
            rollbackCounter.increment(rollbackCount);

            log.debug("15 Step after chunk - job: {} step: {} exitstatus: {}, batchstatus: {}, commits: {}, rollbacks: {}, reads: {}, writes: {}, writecounter: {}, readcounter: {}, commitcounter: {}, rollbackcounter: {}",
                    jobName, stepName, exitStatus.toString(), batchStatus.toString(), commitCount, rollbackCount, readCount, writeCount, writeCounter.count(), readCounter.count(), commitCounter.count(), rollbackCounter.count());


        }
    }

    @AfterChunkError
    public void afterChunkError(final ChunkContext context) {
        if (stepListenerConfig.isLogChunkError()) {
            log.debug("16 Step after chunck error, is complete: {}", context.isComplete());

            StepContext stepContext = context.getStepContext();
            StepExecution stepExecution = stepContext.getStepExecution();
            ExitStatus exitStatus = stepExecution.getExitStatus();
            BatchStatus batchStatus = stepExecution.getStatus();
            String stepName = stepExecution.getStepName();
            JobExecution jobExecution = stepExecution.getJobExecution();
            String jobName = jobExecution.getJobInstance().getJobName();
            int commitCount = stepExecution.getCommitCount();
            int rollbackCount = stepExecution.getRollbackCount();
            int writeCount = stepExecution.getWriteCount();
            int readCount = stepExecution.getReadCount();

            writeCounter.increment(writeCount);
            readCounter.increment(readCount);
            commitCounter.increment(commitCount);
            rollbackCounter.increment(rollbackCount);

            log.debug("15 Step info  chunk - job: {} step: {} exitstatus: {}, batchstatus: {}, commits: {}, rollbacks: {}, reads: {}, writes: {}, writecounter: {}, readcounter: {}, commitcounter: {}, rollbackcounter: {}",
                    jobName, stepName, exitStatus.toString(), batchStatus.toString(), commitCount, rollbackCount, readCount, writeCount, writeCounter.count(), readCounter.count(), commitCounter.count(), rollbackCounter.count());

        }
    }

    @AfterStep
    public ExitStatus afterStep(final StepExecution stepExecution) {
        this.stepExecution = stepExecution;
        if (stepListenerConfig.isLogAfterStep()) {
            log.debug("17 Step after: {} ended with status: {} exit status: {} at: {} ",
                    this.stepExecution.getStepName(),
                    this.stepExecution.getStatus().toString(),
                    this.stepExecution.getExitStatus().toString(),
                    (this.stepExecution.getEndTime() == null ? "(unknown)" : this.stepExecution.getEndTime().toString()));
        }
        return this.stepExecution.getExitStatus();
    }

    private boolean stopConditionsMet() {
        return false;
    }
}
