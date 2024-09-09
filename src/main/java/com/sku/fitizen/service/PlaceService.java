package com.sku.fitizen.service;

import com.sku.fitizen.domain.Place;

public interface PlaceService {
    Place findOrCreatePlace(Long placeId, String name, String roadaddress, String address, String phone);

    Place findPlaceById(Long id);
}
