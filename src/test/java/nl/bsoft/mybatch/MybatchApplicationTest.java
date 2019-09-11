package nl.bsoft.mybatch;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.springframework.test.util.AssertionErrors.assertTrue;


@TestPropertySource(value = {"classpath:/application-test.properties"})
public class MybatchApplicationTest {

    @Test
    public void testApp() {
        assertTrue("Sanity check", true);
    }
}

