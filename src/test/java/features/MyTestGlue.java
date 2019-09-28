package features;

import cucumber.api.java.nl.Als;
import cucumber.api.java.nl.Dan;
import cucumber.api.java.nl.En;
import cucumber.api.java.nl.Gegeven;
import cucumber.api.java8.Nl;
import nl.bsoft.mybatch.TestUtils;
import nl.bsoft.mybatch.config.postgres.repo.BeschikkingsBevoegdheidRepo;
import nl.bsoft.mybatch.controller.JobsController;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.web.servlet.ResultActions;
import util.MyTestContext;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MyTestGlue extends AbstractSpringTest implements Nl {
    private static final Logger logger = LoggerFactory.getLogger(MyTestGlue.class);

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private BeschikkingsBevoegdheidRepo beschikkingsBevoegdheidRepo;

    @Autowired
    private JobsController jobsController;

    private MyTestContext myTestContext = new MyTestContext();

    @Gegeven("^database is geinitialiseerd$")
    public void initializeDatabase() {
        logger.debug("Initialize database");
        CrudRepository[] repos = {
                beschikkingsBevoegdheidRepo
        };

        for (CrudRepository repo : repos) {
            repo.deleteAll();
        }
    }

    @En("^inputbestand \"(.*?)\" bestaat op classpath$")
    public void checkIfFileExistsOnClassPath(final String fileName) {
        logger.debug("Check if file {} exists on classpath", fileName);
        String checkFileName = "classpath:" + fileName;
        Resource resource = resourceLoader.getResource(checkFileName);
        Assert.assertEquals(true, resource.exists());
    }

    @Als("^de gegevens gelezen zijn uit bestand \"([^\"]*)\"$")
    public void readTheData(final String fileName) {
        String url = "/csvtopostgres?fileName=" + fileName;
        logger.debug("Start job csvtopostgres using url: {}", url);

        ResultActions result = null;
        try {
            result = TestUtils.handleGetRequest(jobsController, url);
            result.andExpect(status().is(200));
        } catch (Exception e) {
            logger.error("Data not read during test");
        }
    }

    @Als("^job \"([^\"]*)\" met success gedraaid heeft voor bestand \"([^\"]*)\"$")
    public void startJob(final String jobName, final String fileName) throws Throwable {
        String url = "/" + jobName + "?fileName=" + fileName;
        logger.debug("Start job: {} using url: {}", jobName, url);
        final ResultActions status = TestUtils.handleStatus(jobsController, url);
        status.andExpect(status().is(200));
    }

    @Dan("^zijn er (\\d+) gegevens gelezen$")
    public void checkAantalGelezenGegeven(long aantal) {
        logger.info("Aantal: {}", aantal);
        Assert.assertEquals(aantal, myTestContext.getAantal());
    }

    @Dan("^zijn er (\\d+) gegevens vastgelegd$")
    public void checkAantalVastgelegd(long aantal) {
        Assert.assertEquals(aantal, beschikkingsBevoegdheidRepo.count());
    }

}
