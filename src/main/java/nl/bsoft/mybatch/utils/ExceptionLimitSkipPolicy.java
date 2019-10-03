package nl.bsoft.mybatch.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.step.skip.LimitCheckingItemSkipPolicy;

import java.util.Map;

public class ExceptionLimitSkipPolicy extends LimitCheckingItemSkipPolicy {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionLimitSkipPolicy.class);

    private Class<? extends Exception> exceptionClassToSkip;

    public ExceptionLimitSkipPolicy(int skipLimit, Map<Class<? extends Throwable>, Boolean> skippableExceptions) {
        super(skipLimit, skippableExceptions);
    }

}
