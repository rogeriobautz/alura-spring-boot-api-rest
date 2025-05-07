package med.voll.api.infra.security;

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
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import med.voll.api.domain.usuario.EnumPapelAutorizacao;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

    @Autowired
    private SecurityFilter securityFilter;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers(
                        AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/token"),
                        AntPathRequestMatcher.antMatcher(HttpMethod.GET,"/v3/api-docs/**"),
                        AntPathRequestMatcher.antMatcher(HttpMethod.GET,"/swagger-ui/**"),
                        AntPathRequestMatcher.antMatcher(HttpMethod.GET,"/favicon.ico"))
                    .permitAll()
                    .requestMatchers(
                        AntPathRequestMatcher.antMatcher(HttpMethod.DELETE),
                        AntPathRequestMatcher.antMatcher(HttpMethod.PUT),
                        AntPathRequestMatcher.antMatcher(HttpMethod.POST))
                    .hasRole(EnumPapelAutorizacao.ADMIN.name())
                    .requestMatchers(
                        AntPathRequestMatcher.antMatcher(HttpMethod.GET))
                    .hasAnyRole(
                            EnumPapelAutorizacao.USER.name(),
                            EnumPapelAutorizacao.ADMIN.name())
                    .anyRequest()
                    .authenticated())
            .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
