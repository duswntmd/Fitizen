package com.sku.fitizen.service;

import com.sku.fitizen.domain.Place;
import com.sku.fitizen.mapper.PlaceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlaceServiceImpl implements PlaceService {

    @Autowired
    private PlaceMapper placeMapper;

    @Override
    public Place findOrCreatePlace(Long placeId, String name, String roadaddress, String address, String phone) {
        Place place = placeMapper.findPlaceByNameAndAddress(name, address);

        if (place == null) {
            place = new Place();
            place.setId(placeId);
            place.setName(name);
            place.setRoadaddress(roadaddress);
            place.setAddress(address);
            place.setPhone(phone);
            place.setImageUrl(null);
            placeMapper.insertPlace(place);

//            System.out.println("Created new Place: " + place);
        }

        return place;
    }

    @Override
    public Place findPlaceById(Long id) {
        return placeMapper.findPlaceById(id);
    }
}
