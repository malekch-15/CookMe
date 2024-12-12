package cookme.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Value("${app.url}")
    private String appUrl;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(a -> a
                        .requestMatchers("/api/users/me").permitAll()
                        .requestMatchers(HttpMethod.GET,"api/cookMe").permitAll()
                        .requestMatchers(HttpMethod.DELETE,"/api/cookMe/{id}").authenticated()
                        .requestMatchers(HttpMethod.POST,"/api/cookMe").authenticated()
                        .requestMatchers(HttpMethod.PUT,"/api/cookMe/{id}").authenticated()
                        .anyRequest().permitAll()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
                .exceptionHandling(ex -> ex.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .oauth2Login( oauth->oauth.defaultSuccessUrl(appUrl))
                .logout(logout -> logout.logoutUrl(appUrl));
        return http.build();
    }

}
