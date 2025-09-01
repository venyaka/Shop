package veniamin.shop.backend.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import veniamin.shop.backend.filter.ExceptionHandlerFilter;
import veniamin.shop.backend.filter.RefreshTokenFilter;
import veniamin.shop.backend.filter.jwt.JwtTokenFilter;

import java.util.Arrays;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenFilter jwtTokenFilter;

    private final RefreshTokenFilter refreshTokenFilter;

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
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterAfter(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(refreshTokenFilter, JwtTokenFilter.class)
                .addFilterBefore(exceptionHandlerFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(Customizer.withDefaults())
                .authorizeHttpRequests(c ->
                        c
                                // Доступ к авторизации открыт всем
                                .requestMatchers("/api/authorize/**").permitAll()
                                // Доступ к Swagger открыт всем
                                .requestMatchers("/swagger-ui/**").permitAll()
                                .requestMatchers("/v3/api-docs/**").permitAll()
                                // USER: просмотр категорий и поиск продуктов
                                .requestMatchers(HttpMethod.GET, "/api/categories/**").hasAnyAuthority("USER", "ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/products/**").hasAnyAuthority("USER", "ADMIN")
                                .requestMatchers("/api/users/**").hasAnyAuthority("USER", "ADMIN")
                                // ADMIN: полный доступ к продуктам и категориям
                                .requestMatchers("/api/products/**").hasAuthority("ADMIN")
                                .requestMatchers("/api/categories/**").hasAuthority("ADMIN")
                                .requestMatchers("/api/file/**").hasAuthority("ADMIN")
                                // Остальные запросы запрещены
                                .anyRequest().denyAll()
                )
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .logout(c -> c.invalidateHttpSession(true)
                        .clearAuthentication(true))
                .sessionManagement(c -> c.maximumSessions(1))
                .build();
    }

//    @Bean
//    public static ServletListenerRegistrationBean httpSessionEventPublisher() {
//        return new ServletListenerRegistrationBean(new HttpSessionEventPublisher());
//    }
}