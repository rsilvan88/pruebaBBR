package com.evertec.springboot2.crud.configuration;

import com.evertec.springboot2.crud.jwt.JwtAuthenticationEntryPoint;
import com.evertec.springboot2.crud.jwt.JwtRequestFilter;
import com.evertec.springboot2.crud.jwt.JwtTokenUtil;
import com.evertec.springboot2.crud.service.UsuarioJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private UsuarioJWT myUserDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public JwtRequestFilter jwtRequestFilter() {
        return new JwtRequestFilter(jwtTokenUtil);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeRequests()
            .antMatchers("/auth").permitAll()
            .antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources/**", "/configuration/**", "/swagger-ui.html", "/webjars/**", "/actuator/**").permitAll()
            .antMatchers(HttpMethod.POST, "/validateAuth").authenticated()
            .antMatchers(HttpMethod.GET, "/api/v1/tareas").authenticated()
            .antMatchers(HttpMethod.POST, "/api/v1/tareas").hasRole("ADMIN")
            .antMatchers(HttpMethod.PUT, "/api/v1/tareas/**").hasRole("ADMIN")
            .antMatchers(HttpMethod.DELETE, "/api/v1/tareas/**").hasRole("ADMIN")
            .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            .anyRequest().authenticated().and()
            .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtRequestFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}