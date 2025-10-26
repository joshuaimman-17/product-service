package T.Gopi.Textiles.com.product_service.Config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void initialize() throws Exception {
        String firebaseConfig = System.getenv("FIREBASE_SERVICE_ACCOUNT");

        if (firebaseConfig == null || firebaseConfig.isEmpty()) {
            throw new IllegalStateException("❌ FIREBASE_SERVICE_ACCOUNT environment variable is not set.");
        }

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(
                        new ByteArrayInputStream(firebaseConfig.getBytes(StandardCharsets.UTF_8))
                ))
                .build();

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
            System.out.println("✅ Firebase initialized successfully");
        }
    }
}
