package nl.bsoft.mybatch.config.health;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomHealthIndicator extends AbstractHealthIndicator {

    private MeterRegistry registry = new SimpleMeterRegistry();

    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {
        log.info("Get health");
        // Use the builder to build the health status details that should be reported.
        // If you throw an exception, the status will be DOWN with the exception message.
        builder.up()
                .withDetail("app", "Alive and Kicking")
                .withDetail("error", "Nothing! I'm good.");
    }

    @Bean
    MeterRegistry getRegistry() {
        log.debug("Created meter registry");
        return this.registry;
    }

}
