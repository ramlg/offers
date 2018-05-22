package com.rg.wp.exercise.offers.service;


import com.rg.wp.exercise.offers.model.Offer;
import com.rg.wp.exercise.offers.repository.OfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.Optional.ofNullable;
import static java.util.UUID.randomUUID;

@Service
public class OfferService {

    @Autowired
    private OfferRepository offerRepository;

    public UUID saveOffer(Offer offer) {

        if(offer.getId() != null  &&
                offerRepository.exists(offer.getId())) {
           throw new DuplicateKeyException("Offer with the given Id already exists");

        }else if (offer.getId() == null){
            offer.setId(randomUUID());
        }

        offerRepository.save(offer);
        return offer.getId();
    }

    public Optional<Offer> getOffer(UUID uuid) {

        return ofNullable(offerRepository.findOfferById(uuid));
    }

    public List<Offer> getAllOffers() {

        return offerRepository.findAll();
    }

    public void cancel(UUID offerId) {
        Offer existingOffer = offerRepository.findOfferById(offerId);

        if(existingOffer == null) {
            throw new EmptyResultDataAccessException(1);
        }
        existingOffer.setCancelled(true);
        offerRepository.save(existingOffer);

    }
}

