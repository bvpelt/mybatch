package nl.bsoft.mybatch;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.test.context.TestPropertySource;

import static org.springframework.test.util.AssertionErrors.assertTrue;

@Slf4j
@TestPropertySource(value = {"classpath:/application-test.properties"})
public class MybatchApplicationTest {

    @Test
    public void testApp() {
        assertTrue("Sanity check", true);
    }
}

