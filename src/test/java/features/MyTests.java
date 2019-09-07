package features;

import cucumber.api.java.nl.Als;
import cucumber.api.java.nl.Dan;
import cucumber.api.java.nl.En;
import cucumber.api.java.nl.Gegeven;
import nl.bsoft.mybatch.config.GegevensReader;
import nl.bsoft.mybatch.config.repo.BeschikkingsBevoegdheidRepo;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.repository.CrudRepository;
import util.MyTestContext;

public class MyTests extends AbstractSpringTest {
    private static final Logger logger = LoggerFactory.getLogger(MyTests.class);

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private BeschikkingsBevoegdheidRepo beschikkingsBevoegdheidRepo;

    @Autowired
    public MyTests() {

    }

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

    @Dan("^zijn er (\\d+) gegevens gelezen$")
    public void checkAantalGelezenGegeven(long aantal) {
        logger.info("Aantal: {}", aantal);
        Assert.assertEquals(aantal, myTestContext.getAantal());
    }


}
