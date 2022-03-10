package com.iviberberi.beer.order.service.sm.actions;

import com.iviberberi.beer.order.service.config.JmsConfig;
import com.iviberberi.beer.order.service.domain.BeerOrder;
import com.iviberberi.beer.order.service.domain.BeerOrderEventEnum;
import com.iviberberi.beer.order.service.domain.BeerOrderStatusEnum;
import com.iviberberi.beer.order.service.repositories.BeerOrderRepository;
import com.iviberberi.beer.order.service.services.BeerOrderManagerImpl;
import com.iviberberi.beer.order.service.web.mappers.BeerOrderMapper;
import com.iviberberi.brewery.model.events.AllocateOrderRequest;
import com.iviberberi.brewery.model.events.ValidateOrderRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;


@Slf4j
@Component
@RequiredArgsConstructor
public class AllocateOrderAction implements Action<BeerOrderStatusEnum, BeerOrderEventEnum> {

    private final BeerOrderRepository beerOrderRepository;
    private final BeerOrderMapper beerOrderMapper;
    private final JmsTemplate jmsTemplate;

    @Override
    public void execute(StateContext<BeerOrderStatusEnum, BeerOrderEventEnum> context) {
        String beerOrderId = (String) context.getMessage().getHeaders().get(BeerOrderManagerImpl.ORDER_ID_HEADER);
        Optional<BeerOrder> beerOrderOptional = beerOrderRepository.findById(UUID.fromString(beerOrderId));

        beerOrderOptional.ifPresentOrElse(beerOrder -> {
            jmsTemplate.convertAndSend(JmsConfig.ALLOCATE_ORDER_QUEUE,
                    AllocateOrderRequest.builder()
                            .beerOrderDto(beerOrderMapper.beerOrderToDto(beerOrder))
                            .build());
            log.debug("Sent Allocation Request for order id: " + beerOrderId);
        }, () -> log.error("Beer Order Not Found!"));
    }
}
