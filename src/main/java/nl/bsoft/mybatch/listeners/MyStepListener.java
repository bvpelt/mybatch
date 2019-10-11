package nl.bsoft.mybatch.listeners;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepListener;
import org.springframework.batch.core.annotation.*;
import org.springframework.batch.core.scope.context.ChunkContext;

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
@NoArgsConstructor
public @Data
class MyStepListener<INPUT, OUTPUT> implements StepListener {

    private StepExecution stepExecution;

    @BeforeStep
    public void beforeStep(final StepExecution stepExecution) {
        this.stepExecution = stepExecution;
        log.debug("01 Step before: {} started at: {} ", stepExecution.getStepName(), stepExecution.getStartTime().toString());
        JobParameters jobParameters = this.stepExecution.getJobParameters();
        Map<String, JobParameter> params = jobParameters.getParameters();
        params.forEach((k, v) -> {
            log.debug("01 Step before - Parameter: {}, value: {}", k, v);
        });
    }

    @BeforeChunk
    public void beforeChunk(final ChunkContext context) {
        log.debug("02 Step before chunck");
    }

    @BeforeRead
    public void beforeRead() {
        log.debug("03 Step before read");
    }

    @AfterRead
    public void afterRead(final INPUT item) {
        log.debug("04 Step after read item: {} ", item.toString());
        if (stopConditionsMet()) {
            this.stepExecution.setTerminateOnly();
        }
    }

    @OnReadError
    public void onReadError(final Exception ex) {
        log.debug("05 Step read error: {}", ex.getMessage());
    }

    @OnSkipInRead
    public void onSkipInRead(final Throwable t) {
        log.debug("06 Step after read skip on exception: {}", t.getMessage());
    }

    @BeforeProcess
    public void beforeProcess(final INPUT item) {
        log.debug("07 Step before process of item: {}", item.toString());
    }

    @AfterProcess
    public void afterProcess(
            final INPUT item, final OUTPUT result) {
        log.debug("08 Step after process: {}", stepExecution.getStepName());
    }

    @OnProcessError
    public void onProcessError(final INPUT item, Exception e) {
        log.debug("09 Step after process error: {}", e.getMessage());
    }

    @OnSkipInProcess
    public void onSkipInProcess(final INPUT item, final Throwable t) {
        log.debug("10 Step after skip in proces - item: {}, exception: {}", item.toString(), t.getMessage());
    }

    @BeforeWrite
    public void beforeWrite(final List<? extends OUTPUT> items) {
        log.debug("11 Step before write of {} items", items.size());
        items.forEach((i) -> {
            log.debug("11 Step before write of item: {}", i.toString());
        });
    }

    @AfterWrite
    public void afterWrite(final List<? extends OUTPUT> items) {
        log.debug("12 Step after write of {} items", items.size());
        items.forEach((i) -> {
            log.debug("12 Step after write of item: {}", i.toString());
        });
    }

    @OnWriteError
    public void onWriteError(final Exception exception, final List<? extends OUTPUT> items) {
        log.debug("13 Step on write error of {} items with exception: {}", items.size(), exception.getMessage());
        items.forEach((i) -> {
            log.debug("13 Step on write error of item: {}", i.toString());
        });
    }

    @OnSkipInWrite
    public void onSkipInWrite(final OUTPUT item, final Throwable t) {
        log.debug("14 Step skip in write on item: {}, exception: {}", item.toString(), t.getMessage());
    }

    @AfterChunk
    public void afterChunk(final ChunkContext context) {
        log.debug("15 Step after chunck, is complete: {}", context.isComplete());
    }

    @AfterChunkError
    public void afterChunkError(final ChunkContext context) {
        log.debug("16 Step after chunck error, is complete: {}", context.isComplete());
    }

    @AfterStep
    public void afterStep(final StepExecution stepExecution) {
        this.stepExecution = stepExecution;
        log.debug("17 Step after: {} ended with status: {} exit status: {} at: {} ",
                this.stepExecution.getStepName(),
                this.stepExecution.getStatus().toString(),
                this.stepExecution.getExitStatus().toString(),
                (this.stepExecution.getEndTime() == null ? "(unknown)" : this.stepExecution.getEndTime().toString()));
    }

    private boolean stopConditionsMet() {
        return false;
    }
}
