package com.sankhya.felipebs.api.repository;

import com.sankhya.felipebs.api.entity.OrderItemsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IOrderItemsRepository extends JpaRepository<OrderItemsEntity, Long> {
}
