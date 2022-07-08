package com.hust.sercurity.security;

import com.auth0.jwt.algorithms.Algorithm;
import com.hust.sercurity.filter.AuthenticationFilter;
import com.hust.sercurity.filter.AuthorizationFilter;
import com.hust.sercurity.service.AppUserService;
import com.hust.sercurity.service.loginToken.LoginTokenService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final AppUserService appUserService;
    private final PasswordEncoder passwordEncoder;

    private final LoginTokenService loginTokenService;

    private final Algorithm algorithm;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManagerBean(), loginTokenService, algorithm);
        authenticationFilter.setFilterProcessesUrl("/v1/login");

        http
                .csrf().disable()
                .authorizeRequests()
                    .antMatchers("/v1/register/**","/v1/login","/v1/token/refresh")
                    .permitAll()
                .anyRequest()
                .authenticated().and().logout().and()
                .oauth2Login()
                //.successHandler(); // TODO
                .defaultSuccessUrl("/v1/oauth2/loginSuccess",true);
        http.addFilter(authenticationFilter);
        http.addFilterBefore(new AuthorizationFilter(algorithm), UsernamePasswordAuthenticationFilter.class);

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService((UserDetailsService) appUserService);
        return provider;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}