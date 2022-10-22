package com.abc.senki.service;

import com.abc.senki.model.entity.CommuneEntity;
import com.abc.senki.model.entity.DistrictEntity;
import com.abc.senki.model.entity.ProvinceEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Component
@Service
public interface AddressService {

    List<CommuneEntity> getAllCommuneInDistrict(String id);
    List<DistrictEntity> getAllDistrictInProvince(String id);
    List<ProvinceEntity> getAllProvince();

    CommuneEntity getCommuneById(String id);
    DistrictEntity getDistrictById(String id);
    ProvinceEntity getProvinceById(String id);
}
