package nl.bsoft.mybatch.controller;

import lombok.extern.slf4j.Slf4j;
import nl.bsoft.mybatch.config.GitProperties;
import nl.bsoft.mybatch.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Slf4j
@Controller
public class JobsController {
    private static final Logger logger = LoggerFactory.getLogger(JobsController.class);

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    Job fileToPostgresSkipJob;

    @Autowired
    Job fileToPostgresLimitJob;

    @Autowired
    Job postgres2H2Job;

    @Autowired
    Job file2H2Job;

    @GetMapping("/csvtopostgresskip")
    public String csvtopostgresskip(@RequestParam(value = "fileName") String fileName) throws Exception {
        logger.debug("Start csvtopostgresskip with filename: {}", fileName);

        String result = startCsvJob(fileToPostgresSkipJob, fileName);
        return result;
    }

    @GetMapping("/gitinfo")
    public String gitinfo() throws Exception {
        logger.debug("Start gitinfo");

        GitProperties gitProperties = new GitProperties();

        StringBuffer result = new StringBuffer();
        result.append(GitProperties.BRANCH + ": " + gitProperties.getBranch());
        result.append("\n" + GitProperties.BUILD_TIME + ": " + gitProperties.getBuildTime());
        result.append("\n" + GitProperties.BUILD_USER_EMAIL + ": " + gitProperties.getBuildUserEmail());
        result.append("\n" + GitProperties.BUILD_USER_NAME + ": " + gitProperties.getBuildUserName());
        result.append("\n" + GitProperties.BUILD_VERSION + ": " + gitProperties.getBuildVersion());
        result.append("\n" + GitProperties.CLOSEST_TAG_COMMIT_COUNT + ": " + gitProperties.getClosestTagCommitCount());
        result.append("\n" + GitProperties.CLOSEST_TAG_NAME + ": " + gitProperties.getClosestTagName());
        result.append("\n" + GitProperties.COMMIT_ID + ": " + gitProperties.getCommitId());
        result.append("\n" + GitProperties.COMMIT_ID_ABBREV + ": " + gitProperties.getCommitIdAbbrev());
        result.append("\n" + GitProperties.COMMIT_ID_DESCRIBE + ": " + gitProperties.getCommitIdDescribe());
        result.append("\n" + GitProperties.COMMIT_ID_DESCRIBE_SHORT + ": " + gitProperties.getCommitIdDescribeShort());
        result.append("\n" + GitProperties.COMMIT_MESSAGE_FULL + ": " + gitProperties.getCommitMessageFull());
        result.append("\n" + GitProperties.COMMIT_MESSAGE_SHORT + ": " + gitProperties.getCommitMessageShort());
        result.append("\n" + GitProperties.COMMIT_TIME + ": " + gitProperties.getCommitTime());
        result.append("\n" + GitProperties.COMMIT_USER_EMAIL + ": " + gitProperties.getCommitUserEmail());
        result.append("\n" + GitProperties.COMMIT_USER_NAME + ": " + gitProperties.getCommitUserName());
        result.append("\n" + GitProperties.DIRTY + ": " + gitProperties.getDirty());
        result.append("\n" + GitProperties.HOST + ": " + gitProperties.getHost());
        result.append("\n" + GitProperties.LOCAL_BRANCH_AHEAD + ": " + gitProperties.getLocalBranchAhead());
        result.append("\n" + GitProperties.LOCAL_BRANCH_BEHIND + ": " + gitProperties.getLocalBranchBehind());
        result.append("\n" + GitProperties.REMOTE_ORIGIN_URL + ": " + gitProperties.getRemoteOriginUrl());
        result.append("\n" + GitProperties.TAGS + ": " + gitProperties.getTags());
        result.append("\n" + GitProperties.TOTAL_COMMIT_COUNT + ": " + gitProperties.getTotalCommitCount());

        return result.toString();
    }

    @GetMapping("/csvtopostgreslimit")
    public String csvtopostgreslimit(@RequestParam(value = "fileName") String fileName) throws Exception {
        logger.debug("Start csvtopostgreslimit with filename: {}", fileName);

        String result = startCsvJob(fileToPostgresLimitJob, fileName);
        return result;
    }

    @GetMapping("/postgrestoh2")
    public String postgrestoh2() throws Exception {

        String result = "ready";

        JobParameters parameters = new JobParametersBuilder()
                .addString("key", "empty")
                .toJobParameters();

        try {
            jobLauncher.run(postgres2H2Job, parameters);
        } catch (Exception e) {
            result = "error";
        }
        return result;
    }

    @GetMapping("/csvtoh2")
    public String csvtoh2(@RequestParam(value = "fileName", defaultValue = "") String fileName) throws Exception {

        String result = "/ready";
        long now = System.currentTimeMillis();
        LocalDateTime date = LocalDateTime.ofInstant(Instant.ofEpochMilli(now), ZoneId.systemDefault());
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm:ss.SSS");

        logger.info("Start job with parameters fileName: {}, startTime: {}", fileName, date.format(dateTimeFormatter));

        JobParameters parameters = new JobParametersBuilder()
                .addLong("startTime", System.currentTimeMillis())
                .addString("filename", fileName)
                .toJobParameters();

        try {
            jobLauncher.run(file2H2Job, parameters);
        } catch (Exception e) {
            result = "/error";
        }
        return result;
    }


    @GetMapping("/ready")
    public String handleReady() throws Exception {
        String result = "Ready";
        return result;
    }

    @GetMapping("/error")
    public String handleError() throws Exception {
        String result = "Error";

        return result;
    }

    private String startCsvJob(final Job fileToPostgresSkipJob, final String fileName) throws Exception {
        if ((fileName == null) ||
                (fileName.length() == 0)) {
            throw new Exception("fileName parameter expected but not present");
        }

        String result = "ready";
        long now = System.currentTimeMillis();
        LocalDateTime date = LocalDateTime.ofInstant(Instant.ofEpochMilli(now), ZoneId.systemDefault());
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm:ss.SSS");

        logger.info("Start job with parameters fileName: {}, startTime: {}", fileName, date.format(dateTimeFormatter));

        JobParameters parameters = new JobParametersBuilder()
                .addString("filename", fileName)
                .addDate("startdate", DateUtils.asDate(date))
                .toJobParameters();

        try {
            jobLauncher.run(fileToPostgresSkipJob, parameters);
        } catch (Exception e) {
            result = "error";
        }
        return result;
    }

}