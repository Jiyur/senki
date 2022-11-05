package com.abc.senki.service.implement;


import com.abc.senki.model.entity.AddressEntity;
import com.abc.senki.model.entity.CommuneEntity;
import com.abc.senki.model.entity.DistrictEntity;
import com.abc.senki.model.entity.ProvinceEntity;
import com.abc.senki.repositories.AddressRepository;
import com.abc.senki.repositories.CommuneRepository;
import com.abc.senki.repositories.DistrictRepository;
import com.abc.senki.repositories.ProvinceRepository;
import com.abc.senki.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    final ProvinceRepository provinceRepository;
    final DistrictRepository districtRepository;
    final CommuneRepository communeRepository;
    final AddressRepository addressRepository;

    @Override
    public AddressEntity findById(String id) {
        return addressRepository.findById(id).orElse(null);
    }

    @Override
    public List<AddressEntity> getAll() {
        return addressRepository.findAll().stream().toList();
    }

    @Override
    public AddressEntity saveAddress(AddressEntity address) {
        return addressRepository.save(address);
    }

    @Override
    public void deleteAddress(String id) {
        addressRepository.deleteById(id);
    }

    @Override
    public void saveAddress(String province, String district, String commune, String detail) {

    }

    @Override
    public List<CommuneEntity> getAllCommuneInDistrict(String id) {
        return communeRepository.findAllByDistrict(id).stream().toList();
    }

    @Override
    public List<DistrictEntity> getAllDistrictInProvince(String id) {
        return districtRepository.findAllByProvince(id).stream().toList();
    }

    @Override
    public List<ProvinceEntity> getAllProvince() {
        return provinceRepository.findAll().stream().toList();
    }

    @Override
    public CommuneEntity getCommuneById(String id) {
        return communeRepository.findById(id).orElse(null);
    }

    @Override
    public DistrictEntity getDistrictById(String id) {
        return districtRepository.findById(id).orElse(null);
    }

    @Override
    public ProvinceEntity getProvinceById(String id) {
        return provinceRepository.findById(id).orElse(null);
    }


}
