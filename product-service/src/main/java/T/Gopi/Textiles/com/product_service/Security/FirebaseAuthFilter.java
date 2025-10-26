//package T.Gopi.Textiles.com.product_service.Security;
//
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseToken;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.util.Collections;
//import java.util.List;
//
//@Component
//public class FirebaseAuthFilter extends OncePerRequestFilter {
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//            throws ServletException, IOException {
//
//        String header = request.getHeader("Authorization");
//
//        if (header == null || !header.startsWith("Bearer ")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        String token = header.substring(7);
//
//        try {
//            // Verify the token with Firebase
//            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
//
//            String uid = decodedToken.getUid();
//
//            // Extract the custom role claim (set in Auth Service)
//            String role = (String) decodedToken.getClaims().getOrDefault("role", "GEUST");
//
//            // Map the role to a Spring Security Granted Authority
//            List<SimpleGrantedAuthority> authorities = Collections.singletonList(
//                new SimpleGrantedAuthority("ROLE_" + role.toUpperCase())
//            );
//
//            // Create an Authentication object: Principal is the UID, Authorities include the Role
//            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
//                uid,
//                null,
//                authorities
//            );
//
//            // Set the Authentication object in the Security Context
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        } catch (Exception e) {
//            // Token is invalid, expired, or other Firebase issue
//            System.err.println("Firebase Token Error: " + e.getMessage());
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            return;
//        }
//
//        filterChain.doFilter(request, response);
//    }
//}