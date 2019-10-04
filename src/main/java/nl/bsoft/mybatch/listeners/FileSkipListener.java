package nl.bsoft.mybatch.listeners;

import nl.bsoft.mybatch.csv.Gegeven;
import nl.bsoft.mybatch.database.BeschikkingsBevoegdheid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.annotation.OnSkipInProcess;
import org.springframework.batch.core.annotation.OnSkipInRead;
import org.springframework.batch.core.annotation.OnSkipInWrite;
import org.springframework.batch.item.file.FlatFileParseException;

public class FileSkipListener {
    private static final Logger logger = LoggerFactory.getLogger(FileSkipListener.class);

    @OnSkipInRead
    public void log(Throwable t) {
        if (t instanceof FlatFileParseException) {
            FlatFileParseException ffpe = (FlatFileParseException) t;
            logger.debug("Read error - linenr: {}, input {}", ffpe.getLineNumber(), ffpe.getInput());
        }
    }

    @OnSkipInProcess
    public void log(Gegeven item, Throwable t) {
        logger.debug("Proces error - class: {}, message: {}", t.getClass(), t.getMessage());
    }

    @OnSkipInWrite
    public void log(BeschikkingsBevoegdheid item, Throwable t) {
        logger.debug("Write error - class: {}, message: {}", t.getClass(), t.getMessage());
    }
}

