package com.abc.senki.service.implement;

import com.abc.senki.model.entity.UserEntity;
import com.abc.senki.repositories.UserRepository;
import com.abc.senki.service.NotifyService;
import com.abc.senki.service.UserService;
import com.abc.senki.util.DataUtil;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.firebase.cloud.FirestoreClient;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class NotifyServiceImpl implements NotifyService {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Autowired
    private UserRepository userRepository;


    @Override
    public void pushChargeNotify() {
        Firestore firestore= FirestoreClient.getFirestore();
        List<UserEntity> sellerList = userRepository.getAllSellerNearEndLicense();
        try{
            for (UserEntity seller : sellerList
            ) {
                String sellerId=seller.getId().toString();
                HashMap<String,Object> data=setData(sellerId);
                //Check if document exist
                DocumentReference docRef=firestore.collection("noti").document(sellerId);
                ApiFuture<DocumentSnapshot> future=docRef.get();
                DocumentSnapshot document=future.get();
                if(document.exists()){
                    ArrayList arrayList= (document.getData().get("noti")!=null?(ArrayList) document.getData().get("noti"):new ArrayList());
                    arrayList.add(data);
                    docRef.update("noti",arrayList);

                }


            }
        }
        catch (Exception e){
            System.out.println(e);
        }



        //Add new data to firebase document without delete old data



    }
    public HashMap<String,Object> setData(String sellerId){
        HashMap<String,Object> data=new HashMap<>();
        data.put("date", Timestamp.valueOf(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"))));
        data.put("id",UUID.randomUUID().toString());
        data.put("senderId",sellerId);
        data.put("text","Your license will be expired in 5 days");
        data.put("title","Thông báo về hạn giấy phép kinh doanh");
        return data;
    }
}
