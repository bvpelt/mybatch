package nl.bsoft.mybatch;

import org.junit.Test;
import org.springframework.test.context.TestPropertySource;

import static org.springframework.test.util.AssertionErrors.assertTrue;


@TestPropertySource(value = {"classpath:/application-test.properties"})
public class MybatchApplicationTest {

    @Test
    public void testApp() {
        assertTrue("Sanity check", true);
    }
}

