package veniamin.shop.backend.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import veniamin.shop.backend.filter.ExceptionHandlerFilter;
import veniamin.shop.backend.filter.jwt.JwtTokenFilter;

import java.io.IOException;
import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenFilter jwtTokenFilter;

    private final ExceptionHandlerFilter exceptionHandlerFilter;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PATCH", "DELETE", "PUT"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        configuration.applyPermitDefaultValues();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationEntryPoint htmlAwareEntryPoint() {
        return (request, response, authException) -> {
            String uri = request.getRequestURI();
            String accept = request.getHeader("Accept");
            boolean isHtml = (accept != null && accept.contains(MediaType.TEXT_HTML_VALUE))
                    || uri.startsWith("/admin")
                    || uri.startsWith("/profile")
                    || uri.startsWith("/products")
                    || "/".equals(uri)
                    || uri.endsWith(".html");
            if (isHtml) {
                response.sendRedirect("/login");
            } else {
                // API: отдать 401 без редиректа
                response.setStatus(401);
                response.setContentType("application/json;charset=UTF-8");
                try {
                    response.getWriter().write("{\"status\":401,\"error\":\"UNAUTHORIZED\",\"message\":\"Требуется аутентификация\"}");
                } catch (IOException ignored) {}
            }
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(exceptionHandlerFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(jwtTokenFilter, ExceptionHandlerFilter.class)
                .exceptionHandling(c -> c.authenticationEntryPoint(htmlAwareEntryPoint()))
                .authorizeHttpRequests(c ->
                        c

                                .requestMatchers("/favicon.ico", "/css/**", "/js/**", "/images/**").permitAll()
                                .requestMatchers("/swagger/**", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/webjars/**").permitAll()
                                .requestMatchers("/", "/login", "/register", "/confirm").permitAll()

//                                .requestMatchers("/products/**").permitAll()

                                .requestMatchers("/api/authorize/**").permitAll()

                                .requestMatchers(HttpMethod.GET, "/api/products/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/categories/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/file/**").permitAll()

                                .requestMatchers(HttpMethod.POST, "/api/products/**", "/api/categories/**", "/api/file/**").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/api/products/**", "/api/categories/**", "/api/file/**").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.PATCH, "/api/products/**", "/api/categories/**", "/api/file/**").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/api/products/**", "/api/categories/**", "/api/file/**").hasAuthority("ADMIN")

                                .requestMatchers("/api/users/**").authenticated()

                                .requestMatchers("/profile/**").authenticated()
                                .requestMatchers("/products/**").authenticated()
                                .requestMatchers("/admin/**").hasAuthority("ADMIN")

                                .anyRequest().denyAll()
                )
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .logout(c -> c.invalidateHttpSession(true).clearAuthentication(true))
                .sessionManagement(c -> c.maximumSessions(1))
                .build();
    }
}