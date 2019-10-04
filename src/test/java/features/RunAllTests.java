package features;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import lombok.extern.slf4j.Slf4j;
import org.junit.runner.RunWith;
import org.springframework.test.context.web.WebAppConfiguration;

@Slf4j
@RunWith(Cucumber.class)
@CucumberOptions(tags = {"~@WIP"},
        glue = {"features"},
        plugin = {"pretty", "html:target/cucumber", "json:target/cucumber.json"})
@WebAppConfiguration
public class RunAllTests {
}

