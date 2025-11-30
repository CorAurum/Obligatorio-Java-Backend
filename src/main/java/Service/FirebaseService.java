package Service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.*;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

import java.io.InputStream;

@ApplicationScoped
public class FirebaseService {

    @PostConstruct
    public void init() {
        try {
            InputStream serviceAccount =
                    getClass().getClassLoader().getResourceAsStream("firebase-service-account.json");

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error iniciando Firebase", e);
        }
    }

    public void enviarNotificacion(String token, String titulo, String cuerpo) {
        try {
            Message msg = Message.builder()
                    .setToken(token)
                    .putData("titulo", titulo)
                    .putData("cuerpo", cuerpo)
                    .build();

            FirebaseMessaging.getInstance().send(msg);

        } catch (Exception e) {
            System.err.println("Error enviando push FCM: " + e.getMessage());
        }
    }
}
