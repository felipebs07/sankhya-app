package com.sankhya.felipebs.api.repository;

import com.sankhya.felipebs.api.entity.OrdersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IOrdersRepository extends JpaRepository<OrdersEntity, Long> {
}
