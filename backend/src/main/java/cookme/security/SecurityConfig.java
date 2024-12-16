package cookme.security;

import cookme.repository.AppUserRepo;
import cookme.user.AppUser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

import java.util.Collections;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    @Value("${app.url}")
    private String appUrl;

    private final AppUserRepo appUserRepository;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(a -> a
                        .requestMatchers("/api/users/me").permitAll()
                        .requestMatchers(HttpMethod.GET,"api/cookMe").permitAll()
                        .requestMatchers(HttpMethod.DELETE,"/api/cookMe/{id}").authenticated()
//                        .requestMatchers(HttpMethod.POST,"/api/cookMe").authenticated()
                        .requestMatchers(HttpMethod.PUT,"/api/cookMe/{id}").authenticated()
                        .anyRequest().permitAll()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
                .exceptionHandling(ex -> ex.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .oauth2Login( oauth->oauth.defaultSuccessUrl(appUrl))
                .logout(logout -> logout.logoutSuccessUrl(appUrl));
        return http.build();
    }
    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService() {
        DefaultOAuth2UserService userService = new DefaultOAuth2UserService();

        return userRequest -> {
            OAuth2User githubUser = userService.loadUser(userRequest);

            AppUser user = appUserRepository.findById(githubUser.getName())
                    .orElseGet(() -> {
                        AppUser newUser = new AppUser(
                                githubUser.getName(),
                                githubUser.getAttribute("login").toString(),
                                githubUser.getAttribute("avatar_url").toString(),
                                Collections.emptyList(),Collections.emptyList());
                        return appUserRepository.save(newUser);
                    });

            return githubUser;
        };
    }

}
