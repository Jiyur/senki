package com.abc.senki.service.implement;

import com.abc.senki.model.entity.UserEntity;
import com.abc.senki.service.NotifyService;
import com.abc.senki.util.DataUtil;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class NotifyServiceImpl implements NotifyService {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Override
    public void pushChargeNotify() {
        Firestore firestore= FirestoreClient.getFirestore();


        //Add new data to firebase document without delete old data
        firestore.collection("noti").document("9e049cc6-8e18-4648-ad2d-720f173da2e5")
                .update(UUID.randomUUID().toString(),
                        DataUtil.getData(dateFormat.format(new Date()),"Your need to charge your seller account subcription before 1 day"));


    }
}
