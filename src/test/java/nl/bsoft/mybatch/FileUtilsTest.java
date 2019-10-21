package nl.bsoft.mybatch;

import features.AbstractSpringTest;
import lombok.extern.slf4j.Slf4j;
import nl.bsoft.mybatch.config.GitProperties;
import nl.bsoft.mybatch.controller.JobsController;
import nl.bsoft.mybatch.utils.FileUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import java.io.File;
import java.util.Properties;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@ContextConfiguration(classes = MybatchApplication.class, loader = SpringBootContextLoader.class)
public class FileUtilsTest extends AbstractSpringTest {

    @Autowired
    private JobsController jobsController;

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

    /**
     * Get git info
     */
    @Test
    public void getGitInfo() {
        GitProperties gitProperties = new GitProperties();

        Assert.assertNotNull(gitProperties);
        Properties properties = gitProperties.getGitProperties();
        properties.forEach((k,v) -> {log.info("key: {}, value: {}", k, v);});

        log.info("Branch                  : {}", gitProperties.getBranch());
        log.info("Host                    : {}", gitProperties.getHost());
        log.info("Build time              : {}", gitProperties.getBuildTime());
        log.info("Build user email        : {}", gitProperties.getBuildUserEmail());
        log.info("Build user name         : {}", gitProperties.getBuildUserName());
        log.info("Build version           : {}", gitProperties.getBuildVersion());
        log.info("Closest tag commit count: {}", gitProperties.getClosestTagCommitCount());
        log.info("Closest tag name        : {}", gitProperties.getClosestTagName());
        log.info("Commit id               : {}", gitProperties.getCommitId());
        log.info("Commit id abbrev        : {}", gitProperties.getCommitIdAbbrev());
        log.info("Commit id describe      : {}", gitProperties.getCommitIdDescribe());
        log.info("Commit id describe short: {}", gitProperties.getCommitIdDescribeShort());
        log.info("Commit message full     : {}", gitProperties.getCommitMessageFull());
        log.info("Commit message short    : {}", gitProperties.getCommitMessageShort());
        log.info("Commit time             : {}", gitProperties.getCommitTime());
        log.info("Commit user email       : {}", gitProperties.getCommitUserEmail());
        log.info("Commit user name        : {}", gitProperties.getCommitUserName());
        log.info("Commit dirty            : {}", gitProperties.getDirty());
        log.info("Local branch ahead      : {}", gitProperties.getLocalBranchAhead());
        log.info("Local branch behind     : {}", gitProperties.getLocalBranchBehind());
        log.info("Remote origin url       : {}", gitProperties.getRemoteOriginUrl());
        log.info("Tags                    : {}", gitProperties.getTags());
        log.info("Total commit count      : {}", gitProperties.getTotalCommitCount());
    }

    @Test
    public void checkGitInfo() {
        String url = "/gitinfo";
        log.debug("Get gitinfo using url: {}", url);
        ResultActions status = null;
        try {
            status = TestUtils.handleStatus(jobsController, url);
            status.andExpect(status().is(200));
        } catch (Exception e) {
            log.error("Couldnot get job info: {}", e);
        }
        Assert.assertNotNull(status);
    }
}
