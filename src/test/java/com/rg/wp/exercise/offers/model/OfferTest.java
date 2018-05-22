package com.rg.wp.exercise.offers.model;

import org.junit.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static java.time.Instant.now;
import static java.time.temporal.ChronoUnit.DAYS;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


public class OfferTest {

    @Test
    public void getStatusWillReturnLiveWhenOfferIsNotExpiredAndNotCancelled() {
        Offer offer = offer(now().plus(1, DAYS));
        assertThat(offer.getStatus(), is("Live"));
    }

    @Test
    public void getStatusWillReturnExpiredWhenOfferHasExpiredAndNotCancelled() {
        Offer offer = offer(now().plus(-1, DAYS));
        assertThat(offer.getStatus(), is("Expired"));
    }

    @Test
    public void getStatusWillReturnLiveWhenOfferHasNullEndDate() {
        Offer offer = offer(null);
        assertThat(offer.getStatus(), is("Live"));
    }

    @Test
    public void getStatusWillReturnCancelledWhenOfferIsCancelled() {
        Offer offer = offer(now().plus(1, DAYS));
        offer.setCancelled(true);
        assertThat(offer.getStatus(), is("Cancelled"));
    }

    @Test
    public void getStatusWillReturnCancelledWhenOfferIsCancelledAndExpiredAsWell() {
        Offer offer = offer(now().plus(-1, DAYS));
        offer.setCancelled(true);
        assertThat(offer.getStatus(), is("Cancelled"));
    }

    private Offer offer(Instant endDate) {
        Offer offer = new Offer();
        offer.setId(UUID.randomUUID());
        offer.setEndDate(endDate);
        offer.setPrice(new BigDecimal("50.50"));
        offer.setCurrency("GBP");
        offer.setDescription("offer description");

        return offer;
    }

}