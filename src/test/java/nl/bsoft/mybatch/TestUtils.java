package nl.bsoft.mybatch;

import nl.bsoft.mybatch.controller.JobsController;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

public class TestUtils {
    private static final Logger logger = LoggerFactory.getLogger(TestUtils.class);

    public static ResultActions handleGetRequest(final Object controller, final String url) throws Exception {
        final MockMvc mock = MockMvcBuilders.standaloneSetup(controller).build();
        try {
            return mock.perform(get(url));
        } catch (final MethodArgumentNotValidException e) {
            Assert.fail(e.getMessage());
            return null;
        }
    }

    public static ResultActions handleStatus(final JobsController jobsController, String url) throws Exception {
        return handleGetRequest(jobsController, url);
    }

}
