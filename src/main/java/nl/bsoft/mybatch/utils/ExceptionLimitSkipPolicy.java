package nl.bsoft.mybatch.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.step.skip.LimitCheckingItemSkipPolicy;

import java.util.Map;

@Slf4j
public class ExceptionLimitSkipPolicy extends LimitCheckingItemSkipPolicy {

    private Class<? extends Exception> exceptionClassToSkip;

    public ExceptionLimitSkipPolicy(int skipLimit, Map<Class<? extends Throwable>, Boolean> skippableExceptions) {
        super(skipLimit, skippableExceptions);
    }

}
