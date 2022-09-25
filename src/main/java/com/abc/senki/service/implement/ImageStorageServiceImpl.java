package com.abc.senki.service.implement;

import com.abc.senki.service.ImageStorageService;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ImageStorageServiceImpl implements ImageStorageService {
    private static final String RESOURCE_TYPE = "resource_type";
    private static final String UPLOAD_PRESET = "upload_preset";
    @Override
    public boolean isImageFile(MultipartFile file) {
        return Arrays.asList(new String[] {"image/png","image/jpg","image/jpeg", "image/bmp"})
                .contains(file.getContentType().trim().toLowerCase());
    }

    public Cloudinary cloudinary(){
        return  new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dddmdgm0w",
                "api_key", "268346896576755",
                "api_secret", "4muXOci9jibh6RGN_ywRI5s6qbo"));
    }

    @Override
    public String saveAvatar(MultipartFile file, String fileName){
        Map r;
        try {
            r = this.cloudinary().uploader().upload(file.getBytes(), ObjectUtils.asMap(RESOURCE_TYPE,"auto",UPLOAD_PRESET,"senki_avatar","public_id","senki_avatar/"+fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return (String) r.get("secure_url");
    }
    @Override
    public String saveLogo(MultipartFile file, String fileName){
        Map r;
        try {
            r = this.cloudinary().uploader().upload(file.getBytes(), ObjectUtils.asMap(RESOURCE_TYPE,"auto",UPLOAD_PRESET,"img_avatar","public_id","tiki_logo/"+fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return (String) r.get("secure_url");
    }

    @Override
    public String saveProductImg(MultipartFile file, String fileName){
        Map r;
        try {
            r = this.cloudinary().uploader().upload(file.getBytes(), ObjectUtils.asMap(RESOURCE_TYPE,"auto",UPLOAD_PRESET,"img_product","public_id","senki_product/"+fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return (String) r.get("secure_url");
    }
    @Override
    public String saveImgProduct(MultipartFile file, String fileName){
        Map r;
        try {
            r = this.cloudinary().uploader().upload(file.getBytes(), ObjectUtils.asMap(RESOURCE_TYPE,"auto",UPLOAD_PRESET,"img_product","public_id","tiki_product/"+fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return (String) r.get("secure_url");
    }
    @Override
    public void destroyProductImg(UUID fileName){
        Map r;
        try {
            r = ObjectUtils.asMap("invalidate", true );
            this.cloudinary().api().deleteResourcesByPrefix("senki_product/"+String.valueOf(fileName),r);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String saveProductRatingImg(MultipartFile file, String fileName) {
        Map r;
        try {
            r = this.cloudinary().uploader().upload(file.getBytes(), ObjectUtils.asMap(RESOURCE_TYPE,"auto",UPLOAD_PRESET,"img_avatar","public_id","tiki_product_rating/"+fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return (String) r.get("secure_url");
    }

}
