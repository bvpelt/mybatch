package nl.bsoft.mybatch;

import nl.bsoft.mybatch.utils.FileUtils;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class FileUtilsTest {
    private static final Logger logger = LoggerFactory.getLogger(FileUtilsTest.class);

    /**
     * Find existing file on classpath
     */
    @Test
    public void findFile() {
        String fileToTind = "BRPLand.csv";
        File fullFileName = FileUtils.search(fileToTind);
        logger.info("Full filename: {}", fullFileName.toString());
        Assert.assertEquals(fileToTind, fullFileName.getName());

    }

    /**
     * Find not existing file on classpath
     */
    @Test
    public void findFileNotExists() {
        String fileToTind = "BRPLandxxx.csv";
        File fullFileName = FileUtils.search(fileToTind);
        logger.info("Full filename: {}", (fullFileName == null)? "null": fullFileName.toString());
        Assert.assertEquals(null, fullFileName);
    }
}
