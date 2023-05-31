package com.abc.senki;

import com.abc.senki.util.DataUtil;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

@SpringBootApplication()
@RequiredArgsConstructor
@EnableWebMvc
@EnableScheduling
public class SenkiApplication {

    public static void main(String[] args) throws IOException {

        SpringApplication.run(SenkiApplication.class, args);
    }

}
