package com.sankhya.felipebs.api.service;

import com.sankhya.felipebs.api.dto.OrderItemsDto;
import com.sankhya.felipebs.api.dto.ProductsDto;
import com.sankhya.felipebs.api.entity.ProductsEntity;
import com.sankhya.felipebs.api.exception.custom.EntityNotFoundException;
import com.sankhya.felipebs.api.exception.custom.InsufficientQuantityProductException;
import com.sankhya.felipebs.api.repository.IOrdersRepository;
import com.sankhya.felipebs.api.repository.IProductsRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrdersService {
    private final IOrdersRepository ordersRepository;
    private final IProductsRepository productsRepository;

    public OrdersService(IOrdersRepository ordersRepository, IProductsRepository productsRepository) {
        this.ordersRepository = ordersRepository;
        this.productsRepository = productsRepository;
    }

    @Transactional
    public ResponseEntity<OrderItemsDto> createOrder(OrderItemsDto orderItemsDto) {

            if(orderItemsDto.items().isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            List<ProductsEntity> products = new ArrayList<>();
            orderItemsDto.items().forEach(item -> {
                 ProductsEntity product = productsRepository
                        .findById(item.productId())
                        .orElseThrow(() -> new EntityNotFoundException(String.format("Ocorreu um erro ao tentar encontrar o product %s", item.productId())));

                verifyQuantityAvaliableProduct(product, item);
                products.add(product);
            });

        return null;
    }

    private void verifyQuantityAvaliableProduct(ProductsEntity productsEntity, ProductsDto productsDto) {
        if(productsEntity.getStock() < productsDto.quantity()) {
            String msg = String.format(
                    "O produto %s possui apenas %s em estoque, quantidade inferior à solicitada (%s).",
                    productsEntity.getName(),
                    productsEntity.getStock(),
                    productsDto.quantity()
            );
            throw new InsufficientQuantityProductException(msg);
        }
    }
}
