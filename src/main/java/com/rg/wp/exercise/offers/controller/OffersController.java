package com.rg.wp.exercise.offers.controller;


import com.rg.wp.exercise.offers.model.Command;
import com.rg.wp.exercise.offers.model.Offer;
import com.rg.wp.exercise.offers.service.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.UUID;

import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PATCH;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;


@RestController
@RequestMapping("/offers")
public class OffersController {

    @Autowired
    private OfferService offerService;

    @RequestMapping(method = POST)
    public ResponseEntity createOffer(@RequestBody Offer offer) {


        try {
            UUID savedOfferId = offerService.saveOffer(offer);
            URI newOfferLocation = fromCurrentRequest()
                    .path("/{offerId}")
                    .buildAndExpand(savedOfferId)
                    .toUri();
            return created(newOfferLocation)
                    .build();
        }catch(DuplicateKeyException dke) {
            return status(HttpStatus.CONFLICT)
                    .build();
        }

    }

    @RequestMapping(value="/{offerId}")
    public ResponseEntity getOffer(@PathVariable("offerId") UUID offerId) {

        return offerService.getOffer(offerId)
                .map(offer -> ok()
                        .body(offer))
                .orElse(notFound()
                        .build());
    }


    @RequestMapping(method = GET)
    public ResponseEntity getAllOffers() {

        return ok().body(
                offerService.getAllOffers());
    }

    @RequestMapping(value="/{offerId}", method = PATCH)
    public ResponseEntity cancel(@PathVariable("offerId") UUID offerId,
                                 @RequestBody Command command) {
        try {
            if(command.cancelCommand()) {
                offerService.cancel(offerId);
                return ok().build();
            }else {
                return badRequest().build();
            }
        }catch(EmptyResultDataAccessException re) {
            return notFound().build();
        }
    }

}
