package com.abc.senki.service.implement;

import com.abc.senki.model.entity.UserEntity;
import com.abc.senki.model.payload.RevenueDTO;
import com.abc.senki.repositories.StaticRepository;
import com.abc.senki.service.StaticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class StaticServiceImpl implements StaticService {
    @Autowired
    StaticRepository staticRepository;
    @Override
    public List<RevenueDTO> getStaticBySeller(UserEntity seller) {
        return staticRepository.findAllBySeller(seller.getId());
    }

    @Override
    public List<RevenueDTO> getStaticByYear(UserEntity seller,String year) {
        return staticRepository.findAllByYear(seller.getId(),year);
    }
}
