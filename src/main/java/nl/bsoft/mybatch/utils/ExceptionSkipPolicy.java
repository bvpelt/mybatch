package nl.bsoft.mybatch.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;

@Slf4j
public class ExceptionSkipPolicy implements SkipPolicy {

    private Class<? extends Exception> exceptionClassToSkip;

    public ExceptionSkipPolicy(
            Class<? extends Exception> exceptionClassToSkip) {
        super();
        this.exceptionClassToSkip = exceptionClassToSkip;
    }


    @Override
    public boolean shouldSkip(Throwable throwable, int i) throws SkipLimitExceededException {
        boolean result = this.exceptionClassToSkip.isAssignableFrom(throwable.getClass());
        log.debug("Should skip exception: {}, count: {}, shouldskip: {}", throwable.getClass().getName(), i, result);
        return result;
    }

}
