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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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
    public String gitinfo(Model model) throws Exception {
        logger.debug("Start gitinfo");

        GitProperties gitProperties = new GitProperties();

        List<String> gitLines = new ArrayList<String>();

        gitProperties.GIT_PROPERTIES.forEach((propertyName) -> {
            gitLines.add(propertyName + ": " + gitProperties.getPropertie(propertyName));
        });

        model.addAttribute("gitLines", gitLines);
        return "/gitinfodetail";
    }

    @GetMapping("/csvtopostgreslimit")
    public String csvtopostgreslimit(@RequestParam(value = "fileName") String fileName) throws Exception {
        logger.debug("Start csvtopostgreslimit with filename: {}", fileName);

        String result = startCsvJob(fileToPostgresLimitJob, fileName);
        return result;
    }

    @GetMapping("/postgrestoh2")
    public String postgrestoh2() throws Exception {

        String result = "/ready";

        JobParameters parameters = new JobParametersBuilder()
                .addString("key", "empty")
                .toJobParameters();

        try {
            jobLauncher.run(postgres2H2Job, parameters);
        } catch (Exception e) {
            result = "/error";
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

        String result = "/ready";
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
            log.error("Error in job fileToPostgresSkipJob");
            result = "/error";
        }
        return result;
    }

}