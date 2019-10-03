package nl.bsoft.mybatch.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.step.skip.LimitCheckingItemSkipPolicy;
import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;

import java.util.Map;

public class ExceptionSkipPolicy implements SkipPolicy {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionSkipPolicy.class);

    private Class<? extends Exception> exceptionClassToSkip;

    public ExceptionSkipPolicy(
            Class<? extends Exception> exceptionClassToSkip) {
        super();
        this.exceptionClassToSkip = exceptionClassToSkip;
    }


    @Override
    public boolean shouldSkip(Throwable throwable, int i) throws SkipLimitExceededException {
        boolean result = this.exceptionClassToSkip.isAssignableFrom(throwable.getClass());
        logger.debug("Should skip exception: {}, count: {}, shouldskip: {}", throwable.getClass().getName(), i, result);
        return result;
    }

}
