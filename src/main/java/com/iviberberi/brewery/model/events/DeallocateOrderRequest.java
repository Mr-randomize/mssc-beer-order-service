package com.iviberberi.brewery.model.events;

import com.iviberberi.brewery.model.BeerOrderDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeallocateOrderRequest {

    private BeerOrderDto beerOrderDto;
}
