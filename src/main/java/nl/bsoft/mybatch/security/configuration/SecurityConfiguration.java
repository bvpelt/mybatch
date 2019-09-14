package nl.bsoft.mybatch.security.configuration;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(SecurityConfiguration.class);

    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.inMemoryAuthentication()
                .withUser("testuser").password("12345").roles("USER").and()
                .withUser("admin").password("geheim").roles("USER", "ADMIN");

        logger.info("Configured in memory security");
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/console/**").permitAll()
                .and().httpBasic();
        ;
        httpSecurity.csrf().disable();
        httpSecurity.headers().frameOptions().disable();
        logger.info("Configured httpsecurity");
    }

    @SuppressWarnings("deprecation")
    @Bean
    public static NoOpPasswordEncoder passwordEncoder() {
        logger.info("Created passwordEncoder()");
        return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
    }

}
