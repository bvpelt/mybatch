package nl.bsoft.mybatch.config.security;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.actuate.context.ShutdownEndpoint;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

@Slf4j
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @SuppressWarnings("deprecation")
    @Bean
    public static NoOpPasswordEncoder passwordEncoder() {
        log.info("Created passwordEncoder()");
        return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
    }

    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.inMemoryAuthentication()
                .withUser("testuser").password("12345").roles("USER").and()
                .withUser("admin").password("geheim").roles("USER", "ADMIN", "ACTUATOR_ADMIN");

        log.info("Configured in memory security");
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeRequests()
                .requestMatchers(EndpointRequest.to(ShutdownEndpoint.class))
                .hasRole("ACTUATOR_ADMIN")
                .requestMatchers(EndpointRequest.toAnyEndpoint())
                .permitAll()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                .permitAll()
                .antMatchers("/console/**").permitAll()
                .antMatchers("/jobs/**").permitAll()
                .antMatchers("/joblauncher/**").permitAll()
                .antMatchers("/**").permitAll()
                .and().httpBasic();
        httpSecurity.csrf().disable();
        httpSecurity.headers().frameOptions().disable();
        log.info("Configured httpsecurity");
    }

}
