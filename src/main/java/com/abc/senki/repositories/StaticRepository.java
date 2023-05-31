package com.abc.senki.repositories;

import com.abc.senki.model.payload.RevenueDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.UUID;

public interface StaticRepository extends PagingAndSortingRepository<RevenueDTO, UUID> {
    List<RevenueDTO> findAllBySeller(UUID seller);

    @Query(value = "select * from static_view where seller=?1 and substring(dtime from 1 for 4) = ?2",nativeQuery = true)
    List<RevenueDTO> findAllByYear(UUID sellerId,String year);
}
