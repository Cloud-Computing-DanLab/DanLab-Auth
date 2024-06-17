package com.example.dlauth.config;

import com.example.dlauth.api.service.LogoutService;
import com.example.dlauth.config.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                // Permit all requests without any authentication or authorization checks
                                .anyRequest().permitAll()
                )
                .sessionManagement(sessionManagement ->
                        // Use stateless session management (no session will be created)
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .logout(logout ->
                        // Configure logout URL and behavior if needed
                        logout
                                .disable() // Disable logout for simplicity
                );

        return http.build();
    }
}



//@EnableWebSecurity
//@Configuration
//@RequiredArgsConstructor
//public class SecurityConfig {
//    private final AuthenticationProvider authenticationProvider;
//    private final JwtAuthenticationFilter jwtAuthenticationFilter;
//    private final LogoutService logoutService;
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(authorizeHttpRequest ->
//                        authorizeHttpRequest
//                                // Swagger 추가
//                                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**").permitAll()
//                                // register
//                                .requestMatchers("/auth/register").hasAnyAuthority("UNAUTH")
//                                // update
//                                .requestMatchers("/auth/update").hasAnyAuthority("USER", "ADMIN")
//                                // UnAuth Area
//                                .requestMatchers("/auth/**").permitAll()
//                                // Others
//                                .anyRequest().authenticated()
//                )
//                .sessionManagement((sessionManagement) ->
//                        sessionManagement
//                                // JWT 토큰 기반의 인증을 사용하기 위해 무상태 세션 정책 사용
//                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                )
//                .authenticationProvider(authenticationProvider)
//                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
//                .logout(logoutConfig -> {
//                    logoutConfig
//                            .logoutUrl("/auth/logout")
//                            .addLogoutHandler(logoutService)
//                            .logoutRequestMatcher(new AntPathRequestMatcher("/auth/logout", "GET"))
//                            .logoutSuccessHandler((request, response, authentication)
//                                    -> SecurityContextHolder.clearContext()
//                            );
//                });
//
//        return http.build();
//    }
//}
