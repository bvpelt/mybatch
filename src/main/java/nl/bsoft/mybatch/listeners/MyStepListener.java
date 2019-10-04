package nl.bsoft.mybatch.listeners;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.bsoft.mybatch.csv.Gegeven;
import nl.bsoft.mybatch.database.BeschikkingsBevoegdheid;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.*;
import org.springframework.batch.core.scope.context.ChunkContext;

import java.util.List;

/**
 * See https://docs.spring.io/spring-batch/trunk/apidocs/org/springframework/batch/core/listener/StepListenerSupport.html
 * vor documentation of step listener interface
 *
 * T is the source type in a step definition
 * S is the destination type in a step definition
 */

@Slf4j
@NoArgsConstructor
public @Data
class MyStepListener<T, S>  {

    private StepExecution stepExecution;

    @BeforeStep
    public void beforeStep(final StepExecution stepExecution) {
        this.stepExecution = stepExecution;
        log.debug("01 Step before: {} started at: {} ", stepExecution.getStepName(), stepExecution.getStartTime().toString());
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
    public void afterRead(final T item) {
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
    public void beforeProcess(final T  item) {
        log.debug("07 Step before process of item: {}", item.toString());
    }

    @AfterProcess
    public void afterProcess(
            final T  item, final S result) {
        log.debug("08 Step after process: {}", stepExecution.getStepName());
    }

    @OnProcessError
    public void onProcessError(final T  item, Exception e) {
        log.debug("09 Step after process error: {}", e.getMessage());
    }

    @OnSkipInProcess
    public void onSkipInProcess(final T item, final Throwable t) {
        log.debug("10 Step after skip in proces - item: {}, exception: {}", item.toString(), t.getMessage());
    }

    @BeforeWrite
    public void beforeWrite(final List<? extends S > items) {
        log.debug("11 Step before write of {} items", items.size());
        for (S item: items) {
            log.debug("11 Step before write of item: {}", item.toString());
        }
    }

    @AfterWrite
    public void afterWrite(final List<? extends S>  items) {
        log.debug("12 Step after write of {} items", items.size());
        for (S item: items) {
            log.debug("12 Step after write of item: {}", item.toString());
        }
    }

    @OnWriteError
    public void onWriteError(final Exception exception, final List<? extends S> items) {
        log.debug("13 Step on write error of {} items with exception: {}", items.size(), exception.getMessage());
        for (S item: items) {
            log.debug("13 Step on write error of item: {}", item.toString());
        }
    }
    @OnSkipInWrite
    public void onSkipInWrite(final S item, final Throwable t) {
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
