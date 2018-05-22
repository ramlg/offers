package com.rg.wp.exercise.offers.service;

import com.rg.wp.exercise.offers.model.Offer;
import com.rg.wp.exercise.offers.repository.OfferRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OfferServiceTest {

    @Mock
    private OfferRepository offerRepository;

    @InjectMocks
    private OfferService offerService;

    ArgumentCaptor<Offer> offerArgumentCaptor = ArgumentCaptor.forClass(Offer.class);
    private UUID offerId = UUID.randomUUID();

    @Test
    public void saveOfferWillCallSaveOnRepositoryService() {
        Offer offerToSave = offer(null);
        offerService.saveOffer(offerToSave);

        verify(offerRepository).save(offerToSave);
    }

    @Test
    public void saveOfferWillCallSaveOnRepositoryServiceWithANewUUIDWhenUUIDIsNull() {
        Offer offerToSave = offer(null);
        UUID saveOfferId = offerService.saveOffer(offerToSave);

        verify(offerRepository).save(offerArgumentCaptor.capture());

        UUID capturedId = offerArgumentCaptor.getValue().getId();
        assertThat(capturedId, is(notNullValue()));
        assertThat(saveOfferId, is(capturedId));
    }

    @Test
    public void saveOfferWillSaveOfferWithoutErrorWhenOfferHasUUIDValueSet() {

        Offer offerToSave = offer(offerId);
        UUID savedOfferId = offerService.saveOffer(offerToSave);

        assertThat(savedOfferId, is(offerId));
        verify(offerRepository).save(offerToSave);
    }

    @Test(expected = DuplicateKeyException.class)
    public void saveOfferWillThrowDuplicateKeyExceptionWhenKeyExistsInRepository() {

        when(offerRepository.exists(offerId)).thenReturn(true);

        offerService.saveOffer(offer(offerId));
    }

    @Test
    public void getOfferWillReturnTheOfferReturnedByRepository() {
        Offer existedOffer = offer(offerId);
        when(offerRepository.findOfferById(offerId)).thenReturn(existedOffer);

        Optional<Offer> offer = offerService.getOffer(offerId);

        assertThat(offer.isPresent(), is(true));
        assertThat(offer.get(), is(existedOffer));
    }

    @Test
    public void getOfferWillReturnEmptyOptionalWhenRepositoryDoesNotHaveAnOfferForGivenId() {

        when(offerRepository.findOfferById(offerId)).thenReturn(null);

        Optional<Offer> offer = offerService.getOffer(offerId);

        assertThat(offer.isPresent(), is(false));
    }

    @Test
    public void getAllOffersWillReturnEmptyListWhenRepositoryIsEmpty() {

        when(offerRepository.findAll()).thenReturn(emptyList());

        List<Offer> offers = offerService.getAllOffers();

        assertThat(offers.isEmpty(), is(true));
    }

    @Test
    public void getAllOffersWillReturnAListOfOffersFromRepository() {

        List<Offer> list = Arrays.asList(offer(offerId));
        when(offerRepository.findAll()).thenReturn(list);

        List<Offer> offers = offerService.getAllOffers();

        assertThat(offers, is(list));
    }

    @Test
    public void cancelWillCancelTheOfferWhenOfferExisted() {
        Offer existedOffer = offer(offerId);
        when(offerRepository.findOfferById(offerId)).thenReturn(existedOffer);

        offerService.cancel(offerId);

        assertThat(existedOffer.isCancelled(), is(true));
        verify(offerRepository).findOfferById(offerId);
        verify(offerRepository).save(existedOffer);

    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void cancelWillThrowExceptionWhenOfferForGivenIdNotFound() {
        offerService.cancel(offerId);
    }

    private Offer offer(UUID offerId) {
        Offer offer = new Offer();
        offer.setId(offerId);
        offer.setEndDate(Instant.now().plus(5, DAYS));
        offer.setDescription("offer description");

        return offer;
    }

}