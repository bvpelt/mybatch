package nl.bsoft.mybatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
(exclude = {
		DataSourceAutoConfiguration.class,
		HibernateJpaAutoConfiguration.class,
		LiquibaseAutoConfiguration.class,
		BatchAutoConfiguration.class})
//@EnableSwagger2
@EnableAsync
@EnableTransactionManagement
public class MybatchApplication extends SpringBootServletInitializer {
	private static final Logger logger = LoggerFactory.getLogger(MybatchApplication.class);

	private static final Class<MybatchApplication> APP_CLASS = MybatchApplication.class;

	public static void main(String[] args) {
		SpringApplication.run(MybatchApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(final SpringApplicationBuilder application) {
		logger.info("Bezig met configureren...");
		return application.sources(APP_CLASS);
	}
}
