package com.sankhya.felipebs.api.service;

import com.sankhya.felipebs.api.dto.OrderItemsDto;
import com.sankhya.felipebs.api.dto.ProductsDto;
import com.sankhya.felipebs.api.entity.OrderItemsEntity;
import com.sankhya.felipebs.api.entity.OrdersEntity;
import com.sankhya.felipebs.api.entity.ProductsEntity;
import com.sankhya.felipebs.api.exception.custom.EntityNotFoundException;
import com.sankhya.felipebs.api.exception.custom.InsufficientQuantityProductException;
import com.sankhya.felipebs.api.repository.IOrderItemsRepository;
import com.sankhya.felipebs.api.repository.IOrdersRepository;
import com.sankhya.felipebs.api.repository.IProductsRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrdersService {
    private final IOrdersRepository ordersRepository;
    private final IOrderItemsRepository orderItemsRepository;
    private final IProductsRepository productsRepository;

    public OrdersService(IOrderItemsRepository orderItemsRepository, IOrdersRepository ordersRepository, IProductsRepository productsRepository) {
        this.ordersRepository = ordersRepository;
        this.productsRepository = productsRepository;
        this.orderItemsRepository = orderItemsRepository;
    }

    @Transactional
    public OrdersEntity createOrder(OrderItemsDto orderItemsDto) {
            List<ProductsEntity> products = new ArrayList<>();
            List<OrderItemsEntity> listOrderItems = new ArrayList<>();
            BigDecimal total = BigDecimal.ZERO;

            for( ProductsDto item : orderItemsDto.items()) {
                ProductsEntity product = productsRepository
                        .findById(item.productId())
                        .orElseThrow(() -> new EntityNotFoundException(String.format("Ocorreu um erro ao tentar encontrar o product %s", item.productId())));

                verifyQuantityAvaliableProduct(product, item);
                product.setStock(product.getStock() - item.quantity());

                BigDecimal preco = new BigDecimal(product.getPrice().toString());
                BigDecimal quantidade = new BigDecimal(item.quantity());
                BigDecimal subtotal = preco.multiply(quantidade).setScale(2, RoundingMode.HALF_EVEN);
                total = total.add(subtotal);

                products.add(product);
                listOrderItems.add( new OrderItemsEntity(
                        product,
                        item.quantity(),
                        product.getPrice(),
                        subtotal
                        ));
            };

            productsRepository.saveAll(products);

            total = total.setScale(2, RoundingMode.HALF_EVEN);
            OrdersEntity order = ordersRepository.save(new OrdersEntity(total, LocalDateTime.now()));

            listOrderItems.forEach(item -> item.setOrder(order));
            orderItemsRepository.saveAll(listOrderItems);
            return order;
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
