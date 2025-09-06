package com.sankhya.felipebs.api.service;

import com.sankhya.felipebs.api.repository.IOrderItemsRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderItemsService {
    private final IOrderItemsRepository orderItemsRepository;

    public OrderItemsService(IOrderItemsRepository orderItemsRepository) {
        this.orderItemsRepository = orderItemsRepository;
    }
}
