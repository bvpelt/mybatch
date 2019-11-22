package nl.bsoft.mybatch.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import nl.bsoft.mybatch.config.GitProperties;
import nl.bsoft.mybatch.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

@Slf4j
//@RestController
//@RequestMapping("/jobs")
//@Api("STAND mapping service")
@Controller
public class JobsController {
    private static final Logger logger = LoggerFactory.getLogger(JobsController.class);

    private static final String JOB_PARAM = "job";

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job fileToPostgresSkipJob;

    @Autowired
    private Job fileToPostgresLimitJob;

    @Autowired
    private Job postgres2H2Job;

    @Autowired
    private Job file2H2Job;

    /**
     * @param job,     the name of the job to start
     * @param request, additional parameters for this job
     * @throws Exception
     */
    @RequestMapping(value = "/joblauncher", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void launch(@RequestParam String job,
                       HttpServletRequest request) throws Exception {
        JobParametersBuilder builder = extractParameters(request);
        String jobName = request.getParameter(JOB_PARAM);
        Job selectedJob = null;
        switch (jobName) {
            case "postgres2H2Job":
                // check parameters
                selectedJob = postgres2H2Job;
                break;
            case "fileToPostgresSkipJob":
                selectedJob = fileToPostgresSkipJob;
                break;
            case "fileToPostgresLimitJob":
                selectedJob = fileToPostgresLimitJob;
                break;
            default:
                throw new Exception("Job " + jobName + " not known");
        }

        JobExecution execution = null;
        try {
            execution = jobLauncher.run(selectedJob, builder.toJobParameters());
        } catch (final JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
            log.error("MYBATCH-01: - {}", e);
        }

        if (!execution.getStatus().equals(BatchStatus.COMPLETED)) {
            log.error("MYBATCH-02: job {} not completed ", selectedJob.getName());
        }
    }

    private JobParametersBuilder extractParameters(
            HttpServletRequest request) {
        JobParametersBuilder builder = new JobParametersBuilder();
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            if (!JOB_PARAM.equals(paramName)) {
                builder.addString(paramName, request.getParameter(paramName));
            }
        }
        return builder;
    }

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