package nl.bsoft.mybatch.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.annotation.OnSkipInRead;
import org.springframework.batch.item.file.FlatFileParseException;

public class FileSkipListener {
    private static final Logger logger = LoggerFactory.getLogger(FileSkipListener.class);

    @OnSkipInRead
    public void log(Throwable t) {
        if (t instanceof FlatFileParseException) {
            FlatFileParseException ffpe = (FlatFileParseException) t;
            logger.debug("Input: {}, line: {}", ffpe.getInput(), ffpe.getLineNumber());
        }
    }
}

