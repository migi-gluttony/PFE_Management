package ma.estfbs.pfe_management.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
@Configuration
public class SecurityConfiguration {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable) // Disable CSRF protection
            .cors(AbstractHttpConfigurer::disable) // Disable CORS
            .headers(AbstractHttpConfigurer::disable) // Disable headers security
            .authorizeHttpRequests(authorizeRequests -> {
                authorizeRequests.requestMatchers("/api/auth/register").permitAll(); // Allow access to the register endpoint
                authorizeRequests.requestMatchers("/api/etudiant/**").hasRole("ETUDIANT");
                authorizeRequests.requestMatchers("/api/encadrant/**").hasRole("ENCADRANT");
                authorizeRequests.requestMatchers("/api/chefDeDepartement/**").hasRole("CHEF_DE_DEPARTEMENT");
                authorizeRequests.requestMatchers("/api/jury/**").hasRole("JURY");
                authorizeRequests.anyRequest().authenticated(); // Require authentication for all other requests
            })
            .formLogin(formLogin -> formLogin
                .loginPage("/login")
                .permitAll() // Allow access to the login page
            );

        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Use BCrypt for password encoding
    }
}