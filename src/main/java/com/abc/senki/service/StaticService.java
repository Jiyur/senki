package com.abc.senki.service;

import com.abc.senki.model.entity.UserEntity;
import com.abc.senki.model.payload.RevenueDTO;

import java.util.List;

public interface StaticService {
    List<RevenueDTO> getStaticBySeller(UserEntity seller);
    List<RevenueDTO> getStaticByYear(UserEntity seller,String year);
}
