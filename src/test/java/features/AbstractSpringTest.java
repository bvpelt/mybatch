package features;


import lombok.extern.slf4j.Slf4j;
import nl.bsoft.mybatch.MybatchApplication;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import util.OverrideActiveProfilesResolver;

@Slf4j
@ContextConfiguration(classes = MybatchApplication.class, loader = SpringBootContextLoader.class)
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ActiveProfiles(profiles = "dev", resolver = OverrideActiveProfilesResolver.class)
public abstract class AbstractSpringTest {
}


