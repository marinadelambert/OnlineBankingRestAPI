package com.cbarkinozer.onlinebankingrestapi.app.gen.entity;

/** Entity that belongs to a single customer and may only be accessed by that customer. */
public interface CustomerOwnedEntity {

    Long getOwnerCustomerId();
}
