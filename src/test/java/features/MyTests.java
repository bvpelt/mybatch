package features;

import cucumber.api.java.nl.Als;
import cucumber.api.java.nl.Dan;
import cucumber.api.java.nl.Gegeven;
import nl.bsoft.mybatch.config.GegevensReader;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import util.MyTestContext;

public class MyTests extends AbstractSpringTest {

    @Autowired
    ResourceLoader resourceLoader;

    private MyTestContext myTestContext = new MyTestContext();

    @Gegeven("^inputbestand \"(.*?)\" bestaat op classpath$")
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
        Assert.assertEquals(aantal, myTestContext.getAantal());
    }
}
