package com.rg.wp.exercise.offers.controller;

import com.rg.wp.exercise.offers.model.Offer;
import com.rg.wp.exercise.offers.service.OfferService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static java.time.Instant.now;
import static java.time.temporal.ChronoUnit.DAYS;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class OffersControllerTest {

    private MockMvc mockMvc;

    @Mock
    private OfferService offerService;

    @InjectMocks
    private OffersController offersController;

    private UUID offerId = UUID.randomUUID();
    private Instant defaultEndDate = now().plus(5, DAYS);

    @Before
    public void init() {
        initMocks(this);

        mockMvc = MockMvcBuilders
                .standaloneSetup(offersController)
                .build();
    }

    @Test
    public void createOfferWillReturnHttpStatusCreate() throws Exception {
        when(offerService.saveOffer(any())).thenReturn(offerId);
        mockMvc.perform(post("/offers")
                .contentType(APPLICATION_JSON)
                .content("{}"))
                .andExpect(status()
                        .isCreated())
                .andExpect(header()
                        .stringValues("Location", "http://localhost/offers/"+ offerId));
    }

    @Test
    public void createOfferWillErrorWhenOfferIdExists() throws Exception {
        when(offerService.saveOffer(any()))
                .thenThrow(new DuplicateKeyException("duplicate key"));

        mockMvc.perform(post("/offers")
                .contentType(APPLICATION_JSON)
                .content("{}"))
                .andExpect(status()
                        .isConflict());
    }

    @Test
    public void getOfferWillReturnOfferWhenOneReturnedByService() throws Exception {
        when(offerService.getOffer(any()))
                .thenReturn(Optional.of(offer(offerId, defaultEndDate)));

        mockMvc.perform(get("/offers/"+offerId))
                .andExpect(status()
                        .isOk())
                .andExpect(jsonPath("id", is(offerId.toString())))
                .andExpect(jsonPath("status", is("Live")));
    }

    @Test
    public void getOfferWillReturnNotFoundWhenNoneReturnedByService() throws Exception {
        when(offerService.getOffer(any())).thenReturn(Optional.empty());

        mockMvc.perform(get("/offers/"+offerId))
                .andExpect(status()
                        .isNotFound());
    }

    @Test
    public void getOfferWillReturnExpiredOfferWhenTheOfferEndDateIsInPast() throws Exception {
        when(offerService.getOffer(any()))
                .thenReturn(Optional.of(offer(offerId, now().minus(1, DAYS))));

        mockMvc.perform(get("/offers/"+offerId))
                .andExpect(status()
                        .isOk())
                .andExpect(jsonPath("id", is(offerId.toString())))
                .andExpect(jsonPath("status", is("Expired")));
    }

    @Test
    public void getOffersWillReturnListReturnedByService() throws Exception {
        when(offerService.getAllOffers()).thenReturn(Arrays.asList(offer(offerId, defaultEndDate)));

        mockMvc.perform(get("/offers"))
                .andExpect(status()
                        .isOk())
                .andExpect(jsonPath("$.[0].id", is(offerId.toString())));
    }

    @Test
    public void cancelRequestWillCancelTheOffer() throws Exception {

        mockMvc.perform(patch("/offers/"+offerId)
                .contentType(APPLICATION_JSON)
                .content("{\"command\" : \"cancel\"}"))
                .andExpect(status().isOk());

        Mockito.verify(offerService).cancel(offerId);
    }

    @Test
    public void cancelRequestReturnNotFoundWhenNoOfferExistedForGivenId() throws Exception {
        doThrow(new EmptyResultDataAccessException(0))
                .when(offerService)
                .cancel(offerId);
        mockMvc.perform(patch("/offers/"+offerId)
                .contentType(APPLICATION_JSON)
                .content("{\"command\" : \"cancel\"}"))
                .andExpect(status().isNotFound());

        Mockito.verify(offerService).cancel(offerId);
    }

    @Test
    public void cancelRequestReturnBadRequestWhenCommandIsWrong() throws Exception {

        mockMvc.perform(patch("/offers/"+offerId)
                .contentType(APPLICATION_JSON)
                .content("{\"command\" : \"delete\"}"))
                .andExpect(status().isBadRequest());

        Mockito.verify(offerService, never()).cancel(offerId);
    }

    private Offer offer(UUID offerId, Instant endDate) {
        Offer offer = new Offer();
        offer.setId(offerId);
        offer.setEndDate(endDate);
        offer.setPrice(new BigDecimal("50.50"));
        offer.setCurrency("GBP");
        offer.setDescription("offer description");

        return offer;
    }

}