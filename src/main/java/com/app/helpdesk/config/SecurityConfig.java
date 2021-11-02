package com.app.helpdesk.config;

import com.app.helpdesk.security.jwt.JwtConfigurer;
import com.app.helpdesk.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String EMPLOYEE = "EMPLOYEE";
    private static final String MANAGER = "MANAGER";
    private static final String ENGINEER = "ENGINEER";

    @Value("${spring.permitted.url}")
    private String permittedUrl;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.headers().frameOptions().disable();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
        corsConfiguration.setAllowedOrigins(List.of(permittedUrl));
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PUT", "OPTIONS", "PATCH", "DELETE"));
        corsConfiguration.setExposedHeaders(List.of("Authorization"));
        http.cors().configurationSource(request -> corsConfiguration);

        http.
                httpBasic().disable()
                .cors().and().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()
                .antMatchers("/h2-console/**", "/login").permitAll()
                .antMatchers("/tickets").hasAnyRole(EMPLOYEE, MANAGER, ENGINEER)
                .antMatchers("/create-ticket").hasAnyRole(EMPLOYEE, MANAGER)
                .antMatchers("/ticket-info/**").hasAnyRole(EMPLOYEE, MANAGER, ENGINEER)
                .antMatchers("/attachments/**").hasAnyRole(EMPLOYEE, MANAGER)
                .antMatchers("/tickets-draft/**").hasAnyRole(EMPLOYEE, MANAGER)
                .antMatchers("/feedback/**").hasAnyRole(EMPLOYEE, MANAGER)
                .antMatchers("/change-state/**").hasAnyRole(EMPLOYEE, MANAGER, ENGINEER)
                .antMatchers("/comments/**").hasAnyRole(EMPLOYEE, MANAGER, ENGINEER)
                .anyRequest().authenticated()

                .and()
                .apply(new JwtConfigurer(jwtTokenProvider))

                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login");
    }
}
