package nl.bsoft.mybatch.config.postgres;

import io.micrometer.core.instrument.Counter;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import lombok.extern.slf4j.Slf4j;
import nl.bsoft.mybatch.csv.Gegeven;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class GegevensCsvReader implements ItemReader<Gegeven> {
    private static final String WILL_BE_INJECTED = null;
    private PrometheusMeterRegistry prometheusRegistry;
    private Counter fileReaderCounter;

    private FlatFileItemReader<Gegeven> itemReader;

    public GegevensCsvReader(PrometheusMeterRegistry prometheusRegistry) {
        this.prometheusRegistry = prometheusRegistry;
        this.fileReaderCounter = this.prometheusRegistry.counter("fileReader", "aantal", "waarde");
    }
    @Override
    public Gegeven read() throws Exception {
        log.info("Read gegeven");
        Gegeven gegeven = itemReader.read();
        this.fileReaderCounter.increment();
        return gegeven;
    }


}
