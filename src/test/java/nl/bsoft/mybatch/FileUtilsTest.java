package nl.bsoft.mybatch;

import lombok.extern.slf4j.Slf4j;
import nl.bsoft.mybatch.utils.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

@Slf4j
public class FileUtilsTest {

    /**
     * Find existing file on classpath
     */
    @Test
    public void findFile() {
        String fileToTind = "BRPLand.csv";
        File fullFileName = FileUtils.search(fileToTind);
        log.info("Full filename: {}", fullFileName.toString());
        Assert.assertEquals(fileToTind, fullFileName.getName());

    }

    /**
     * Find not existing file on classpath
     */
    @Test
    public void findFileNotExists() {
        String fileToTind = "BRPLandxxx.csv";
        File fullFileName = FileUtils.search(fileToTind);
        log.info("Full filename: {}", (fullFileName == null) ? "null" : fullFileName.toString());
        Assert.assertEquals(null, fullFileName);
    }
}
