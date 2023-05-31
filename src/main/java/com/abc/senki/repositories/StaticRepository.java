package com.abc.senki.repositories;

import com.abc.senki.model.payload.RevenueDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface StaticRepository extends PagingAndSortingRepository<RevenueDTO, RevenueDTO> {

    List<RevenueDTO> findAllBySeller(UUID seller);

    @Query(value = "select distinct * from static_view s where s.seller=:seller and substring(s.dtime from 1 for 4) =:year",nativeQuery = true)
    List<RevenueDTO> findAllByYear(@Param("seller") UUID sellerId, @Param("year") String year);
}
