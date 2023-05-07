package com.abc.senki.security.config;

import com.abc.senki.common.UserPermission;
import com.abc.senki.handler.OAuth2Handler;
import com.abc.senki.security.jwt.AuthEntryPointJwt;
import com.abc.senki.security.service.AppUserDetailService;
import com.abc.senki.security.filter.AuthTokenFilter;
import com.abc.senki.service.CustomOauth2Service;
import com.abc.senki.util.CookieOAuth2RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class AppSecurityConfig {
    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }
    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;
    @Autowired
    AppUserDetailService userDetailsService;
    @Autowired
    OAuth2Handler oAuth2Handler;
    @Autowired
    CustomOauth2Service customOauth2Service;

    @Bean
    public CookieOAuth2RequestRepository cookieAuthorizationRequestRepository() {
        return new CookieOAuth2RequestRepository();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authentication) throws Exception {
        return authentication.getAuthenticationManager();
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf().disable()
                .httpBasic()
                .disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler);
        http
                .authorizeRequests()
                .antMatchers("/api/user/**").hasAnyAuthority(UserPermission.USER_READ.getPermission(),UserPermission.USER_WRITE.getPermission())
                .antMatchers("/api/seller/**").hasAnyAuthority(UserPermission.USER_READ.getPermission(),UserPermission.USER_WRITE.getPermission())
                .antMatchers("/api/admin/**").hasAnyAuthority(UserPermission.ADMIN_READ.getPermission(),UserPermission.ADMIN_WRITE.getPermission())
                .antMatchers("/**").permitAll()
                .and()
                .oauth2Login()
                .authorizationEndpoint()
                .baseUri("/oauth2/authorize")
                .authorizationRequestRepository(cookieAuthorizationRequestRepository())
                .and()
                .userInfoEndpoint()
                .userService(customOauth2Service)
                .and()
                .successHandler(oAuth2Handler);

        ;


        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();

    }

}
