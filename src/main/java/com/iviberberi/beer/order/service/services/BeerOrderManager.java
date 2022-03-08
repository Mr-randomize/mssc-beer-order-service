package com.iviberberi.beer.order.service.services;

import com.iviberberi.beer.order.service.domain.BeerOrder;

public interface BeerOrderManager {

    BeerOrder newBeerOrder(BeerOrder beerOrder);
}
