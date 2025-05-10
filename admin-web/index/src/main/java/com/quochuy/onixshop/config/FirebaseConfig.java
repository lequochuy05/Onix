package com.quochuy.onixshop.config;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.context.annotation.Configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import jakarta.annotation.PostConstruct;


//Cấu hình
@Configuration
public class FirebaseConfig {

    //Tự động chạy initFirebase để kết nối tới firebase
    @PostConstruct 
    public void initFirebase() {
        try {
            // Đọc file kết nối
            InputStream serviceAccount = getClass().getClassLoader().getResourceAsStream("firebase-config.json");

            if (serviceAccount == null) {
                throw new RuntimeException("Firebase config file not found");
            }

            // Khởi tạo Firebase = Tạo đối tượng FirebaseOptions
            FirebaseOptions options = FirebaseOptions.builder()
                    // xác thực kết nối
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    //Kết nối tới Realtime DB
                    .setDatabaseUrl("https://cuoiki1-25-default-rtdb.firebaseio.com") 
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options); //Kết nối đến firebase lần đầu
                System.out.println("Connected firebase!");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
