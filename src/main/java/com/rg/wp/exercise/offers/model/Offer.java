package com.rg.wp.exercise.offers.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static java.time.Instant.now;

public class Offer {

    private UUID id;
    private String description;
    private String currency;
    private BigDecimal price;
    private Instant endDate;

    @JsonIgnore
    private boolean cancelled;


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currencyName) {
        this.currency = currencyName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public String getStatus() {
        if(cancelled) {
            return "Cancelled";
        }
        if(endDate!= null && now().isAfter(endDate)) {
            return "Expired";
        }
        return "Live";
    }
}
