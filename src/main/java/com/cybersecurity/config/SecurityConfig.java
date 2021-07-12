package com.cybersecurity.config;


import com.cybersecurity.filter.AuthorizationFilter;
import com.cybersecurity.filter.CORSFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.csrf().disable();
        http.cors().disable();
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("POST");
        source.registerCorsConfiguration("/**", config);
        http.authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/*").permitAll();
    }

    @Bean(name = "auth")
    public FilterRegistrationBean<AuthorizationFilter> LoggingFilter(){
        FilterRegistrationBean<AuthorizationFilter> registrationBean=new FilterRegistrationBean<>();
        registrationBean.setFilter(new AuthorizationFilter());
        registrationBean.addUrlPatterns("/data/*");
        return registrationBean;
    }
    @Bean(name = "cors")
    public FilterRegistrationBean<CORSFilter> CorsFilter(){
        FilterRegistrationBean<CORSFilter> registrationBean=new FilterRegistrationBean<>();
        registrationBean.setFilter(new CORSFilter());
        registrationBean.addUrlPatterns("/api/*");
        return registrationBean;
    }
}
