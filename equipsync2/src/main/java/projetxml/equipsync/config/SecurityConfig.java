package projetxml.equipsync.config;



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import projetxml.equipsync.Services.BaseXService;
import projetxml.equipsync.Services.UserService;
import projetxml.equipsync.entities.User;
import projetxml.equipsync.security.JwtRequestFilter;

import static org.springframework.security.config.Customizer.withDefaults;
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final JwtRequestFilter authFilter;
    private final BaseXService baseXService;
    public SecurityConfig(JwtRequestFilter authFilter, BaseXService baseXService) {
        this.authFilter = authFilter;
        this.baseXService = baseXService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/api/v1/login","/api/v1/refreshToken","/api/v1/users/me","/**").permitAll()
//                        .requestMatchers("/api/users/hr/**").hasAnyAuthority("ADMIN","HR")
//                        .requestMatchers("/api/users/user/**").hasAnyAuthority("ADMIN","HR","EMPLOYEE")
//
//                        .requestMatchers("/api/user/me/**").hasAnyAuthority("EMPLOYEE","ADMIN","EM","OM")
//                        .requestMatchers("/api/equipments/**").hasAnyAuthority("EM","ADMIN")
//                        .requestMatchers("/api/projects/**").hasAnyAuthority("OM","ADMIN")
//
//                        .anyRequest().hasAuthority("ADMIN")
                )
                .httpBasic(withDefaults()).csrf(AbstractHttpConfigurer::disable)
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }


}
