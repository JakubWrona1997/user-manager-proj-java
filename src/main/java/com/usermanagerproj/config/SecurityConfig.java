package com.usermanagerproj.config;


import com.usermanagerproj.config.filter.AuthenticationFilter;
import com.usermanagerproj.config.filter.ExceptionHandlerFilter;
import com.usermanagerproj.config.filter.JWTAuthorizationFilter;
import com.usermanagerproj.config.manager.CustomAuthOneWayFunction;
import com.usermanagerproj.config.security.SecurityConstants;
import com.usermanagerproj.service.user.UserDetailsServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    private final UserDetailsServiceImpl userDetailsService;
    private final CustomAuthOneWayFunction customAuthenticationManager;
    private final ConfigProperties configProperties;

    @Bean
    public SecurityFilterChain configure (HttpSecurity httpSecurity) throws Exception {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(customAuthenticationManager, configProperties);
        authenticationFilter.setFilterProcessesUrl(SecurityConstants.LOGIN_URL);
        httpSecurity
                .cors().and().csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, SecurityConstants.SWAGGER_URL).permitAll()
                .antMatchers(HttpMethod.GET, SecurityConstants.API_DOCS).permitAll()
                .antMatchers(HttpMethod.POST,SecurityConstants.REGISTER_URL).permitAll()
                .regexMatchers(HttpMethod.GET,SecurityConstants.CONFIRM_URL_REGEX).permitAll()
                .regexMatchers(HttpMethod.GET,SecurityConstants.GENERATE_PASSWORD_URL).permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(new ExceptionHandlerFilter(), AuthenticationFilter.class)
                .addFilter(authenticationFilter)
                .addFilterAfter(new JWTAuthorizationFilter(userDetailsService, configProperties), AuthenticationFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        return httpSecurity.build();
    }
}
