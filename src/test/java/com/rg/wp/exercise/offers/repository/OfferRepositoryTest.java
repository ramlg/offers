package com.rg.wp.exercise.offers.repository;

import com.rg.wp.exercise.offers.model.Offer;
import org.junit.Test;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.time.temporal.ChronoUnit.DAYS;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;


public class OfferRepositoryTest {

    private Map<UUID, Offer> allOffers = new HashMap<>();

    private OfferRepository offerRepository = new OfferRepository(allOffers);

    UUID offerId = UUID.randomUUID();

    @Test
    public void saveOfferWillSaveTheOffer() {

        Offer offer = offer(offerId);
        offerRepository.save(offer);

        assertThat(allOffers.get(offerId), is(offer));
    }

    @Test
    public void findByIdWillReturnNullWhenOfferForIdDoesNotExists() {
        allOffers.put(offerId, offer(offerId));

        Offer offerById = offerRepository.findOfferById(UUID.randomUUID());

        assertThat(offerById, is(nullValue()));
    }

    @Test
    public void findByIdWillReturnTheOfferExistedWithTheId() {
        Offer existedOffer = offer(offerId);
        allOffers.put(offerId, existedOffer);

        Offer retrievedOffer = offerRepository.findOfferById(offerId);

        assertThat(retrievedOffer.getId(), is(offerId));
        assertThat(retrievedOffer, is(existedOffer));

    }

    @Test
    public void findAllWillReturnEmptyListWhenNoOfferExists() {

        List<Offer> allOffers = offerRepository.findAll();
        assertThat(allOffers.size(), is(0));
    }

    @Test
    public void findAllWillReturnAllOffersFromRepository() {
        givenTwoOfferExistedInRepository();

        List<Offer> allOffers = offerRepository.findAll();
        assertThat(allOffers.size(), is(2));
    }

    @Test
    public void existsWillReturnFalseWhenNoOfferExistsForGivenId() {
        allOffers.put(offerId, offer(offerId));

        boolean exists = offerRepository.exists(UUID.randomUUID());

        assertThat(exists, is(false));
    }

    @Test
    public void existsWillReturnTrueWhenOfferExistsForGivenId() {
        allOffers.put(offerId, offer(offerId));

        boolean exists = offerRepository.exists(offerId);

        assertThat(exists, is(true));
    }

    private void givenTwoOfferExistedInRepository() {
        Offer existedOffer = offer(offerId);
        UUID anotherOfferId = UUID.randomUUID();
        Offer anotherOffer = offer(anotherOfferId);
        allOffers.put(this.offerId, existedOffer);
        allOffers.put(anotherOfferId, anotherOffer);
    }



    private Offer offer(UUID offerId) {
        Offer offer = new Offer();
        offer.setId(offerId);
        offer.setDescription("offer description");
        offer.setEndDate(Instant.now().plus(5, DAYS));
        return offer;
    }


}