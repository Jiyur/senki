package com.abc.senki.config;

import com.abc.senki.util.DataUtil;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {


    @Primary
    @Bean
    public FirebaseApp getfirebaseApp() throws IOException {
        InputStream serviceAccount =
                new ClassPathResource("/config-fb.json").getInputStream();

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        FirebaseApp.initializeApp(options);
//        Firestore firestore= FirestoreClient.getFirestore();
//        firestore.collection("noti")
//                .add(DataUtil.getData("123","hahahaha"));
        return FirebaseApp.getInstance();
    }



}
