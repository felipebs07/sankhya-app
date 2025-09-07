package com.sankhya.felipebs.api.controller;

import com.sankhya.felipebs.api.dto.OrderItemsDto;
import com.sankhya.felipebs.api.entity.OrdersEntity;
import com.sankhya.felipebs.api.service.OrdersService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
public class OrdersController {

  private final OrdersService ordersService;
  public OrdersController(OrdersService ordersService) {
      this.ordersService = ordersService;
  }

  @PostMapping
  public ResponseEntity<OrdersEntity> createOrder(@RequestBody OrderItemsDto orderItemsDto) {
      if(orderItemsDto.items().isEmpty()) {
          return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }
      return ResponseEntity.status(HttpStatus.CREATED).body(ordersService.createOrder(orderItemsDto));
  }
}
