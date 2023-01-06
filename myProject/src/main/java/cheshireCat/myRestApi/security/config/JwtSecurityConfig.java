package cheshireCat.myRestApi.security.config;

import cheshireCat.myRestApi.security.filter.JsonEmailPasswordAuthenticationFilter;
import cheshireCat.myRestApi.security.handler.JsonAuthenticationFailureHandler;
import cheshireCat.myRestApi.security.handler.JsonAuthenticationSuccessJWTProvideHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Configuration
@RequiredArgsConstructor
public class JwtSecurityConfig {

    private final ObjectMapper objectMapper;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

        return new ProviderManager(daoAuthenticationProvider);
    }

    @Bean
    public JsonAuthenticationSuccessJWTProvideHandler jsonAuthenticationSuccessJWTProvideHandler() {
        return new JsonAuthenticationSuccessJWTProvideHandler();
    }

    @Bean
    public JsonAuthenticationFailureHandler jsonAuthenticationFailureHandler() {
        return new JsonAuthenticationFailureHandler();
    }

    @Bean
    public JsonEmailPasswordAuthenticationFilter jsonEmailPasswordAuthenticationFilter() {
        JsonEmailPasswordAuthenticationFilter jsonEmailPasswordAuthenticationFilter =
                new JsonEmailPasswordAuthenticationFilter(objectMapper);

        jsonEmailPasswordAuthenticationFilter.setAuthenticationManager(authenticationManager());
        jsonEmailPasswordAuthenticationFilter.setAuthenticationSuccessHandler(jsonAuthenticationSuccessJWTProvideHandler());
        jsonEmailPasswordAuthenticationFilter.setAuthenticationFailureHandler(jsonAuthenticationFailureHandler());

        return jsonEmailPasswordAuthenticationFilter;
    }



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .formLogin().disable()
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
                .authorizeHttpRequests()
                .antMatchers("/login", "/signUp", "/").permitAll()
                .anyRequest().authenticated()
        .and()
                .addFilterAfter(jsonEmailPasswordAuthenticationFilter(), LogoutFilter.class);

        return  http.build();
    }

}
