package features;

import cucumber.api.java.nl.Als;
import cucumber.api.java.nl.Dan;
import cucumber.api.java.nl.En;
import cucumber.api.java.nl.Gegeven;
import cucumber.api.java8.Nl;
import nl.bsoft.mybatch.config.GegevensReader;
import nl.bsoft.mybatch.config.repo.BeschikkingsBevoegdheidRepo;
import nl.bsoft.mybatch.controller.JobsController;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;
import util.MyTestContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MyTestGlue extends AbstractSpringTest  implements Nl {
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
        CrudRepository[] repos = {beschikkingsBevoegdheidRepo};

        for (CrudRepository repo : repos) {
            repo.deleteAll();
        }
    }

    @En("^inputbestand \"(.*?)\" bestaat op classpath$")
    public void checkIfFileExistsOnClassPath(String fileName) {
        String checkFileName = "classpath:" + fileName;
        Resource resource = resourceLoader.getResource(checkFileName);
        Assert.assertEquals(true, resource.exists());
        myTestContext.setFileName(fileName);
    }

    @Als("^de gegevens gelezen zijn$")
    public void readTheData() {
        GegevensReader gegevensReader = new GegevensReader();
        long aantal = 0;
        try {
            nl.bsoft.mybatch.csv.Gegeven gegeven = gegevensReader.read();
            while (gegeven != null) {
                aantal++;
                gegeven = gegevensReader.read();
            }
        } catch (Exception e) {

        }
        myTestContext.setAantal(aantal);
    }

    @Als("^jobLauncher met success gedraaid heeft$")
    public void startJob() throws Throwable {
        final ResultActions status = handleStatus(jobsController);
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

    private static ResultActions handleGetRequest(final Object controller, final String url) throws Exception {
        final MockMvc mock = MockMvcBuilders.standaloneSetup(controller).build();
        try {
            return mock.perform(get(url));
        } catch (final MethodArgumentNotValidException e) {
            Assert.fail(e.getMessage());
            return null;
        }
    }

    public static ResultActions handleStatus(final JobsController jobsController) throws Exception {
        return handleGetRequest(jobsController, "/jobLauncher/");
    }
}
