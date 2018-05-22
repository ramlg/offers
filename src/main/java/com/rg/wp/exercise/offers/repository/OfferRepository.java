package com.rg.wp.exercise.offers.repository;


import com.rg.wp.exercise.offers.model.Offer;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class OfferRepository {

    private Map<UUID, Offer> allOffers;

    public OfferRepository() {
        this(new HashMap<>());
    }

    public OfferRepository(Map<UUID, Offer> offerStorage) {
        allOffers = offerStorage;
    }

    public void save(Offer offer) {
        allOffers.put(offer.getId(), offer);
    }

    public Offer findOfferById(UUID offerId) {
        return allOffers.get(offerId);
    }

    public List<Offer> findAll() {
        return new ArrayList<>(allOffers.values());
    }

    public boolean exists(UUID offerId) {
        return allOffers.containsKey(offerId);
    }
}
