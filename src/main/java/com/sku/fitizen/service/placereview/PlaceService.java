package com.sku.fitizen.service.placereview;

import com.sku.fitizen.domain.placereview.Place;

public interface PlaceService {
    Place findOrCreatePlace(Long placeId, String name, String roadaddress, String address, String phone);

    Place findPlaceById(Long id);

}
