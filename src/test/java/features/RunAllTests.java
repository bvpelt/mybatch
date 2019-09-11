package features;

//import cucumber.api.CucumberOptions;
//import cucumber.api.junit.Cucumber;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(Cucumber.class)
//@CucumberOptions(tags = {"~@WIP"},
@CucumberOptions(tags = {"not @WIP"},
        glue = {"features"},
        plugin = {"pretty", "html:target/cucumber", "json:target/cucumber.json"})
@WebAppConfiguration
public class RunAllTests {
}

