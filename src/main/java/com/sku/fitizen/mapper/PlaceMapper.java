package com.sku.fitizen.mapper;

import com.sku.fitizen.domain.Place;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PlaceMapper {

    void insertPlace(Place place);

    Place findPlaceByNameAndAddress(@Param("name") String name, @Param("address") String address);

    Place findPlaceById(Long id);
}
