//package T.Gopi.Textiles.com.product_service.Config;
//
//
//import T.Gopi.Textiles.com.product_service.Security.FirebaseAuthFilter;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    private final FirebaseAuthFilter firebaseAuthFilter;
//
//    public SecurityConfig(FirebaseAuthFilter firebaseAuthFilter) {
//        this.firebaseAuthFilter = firebaseAuthFilter;
//    }
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                // 1. Disable CSRF as we are using token-based authentication (stateless)
//                .csrf(csrf -> csrf.disable())
//
//                // 2. Set session management to stateless
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//
//                // 3. Define authorization rules
//                .authorizeHttpRequests(auth -> auth
//                        // Allow health check endpoint if needed (optional)
//                        .requestMatchers("/actuator/**").permitAll()
//                        // All other requests must pass through the Firebase filter
//                        .anyRequest().authenticated()
//                );
//
//        // 4. Add the Firebase token filter before the standard authentication filter
//        http.addFilterBefore(firebaseAuthFilter, UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }
//}
