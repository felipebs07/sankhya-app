package com.sankhya.felipebs.api.unit;

import com.sankhya.felipebs.api.dto.OrderItemsDto;
import com.sankhya.felipebs.api.dto.ProductsDto;
import com.sankhya.felipebs.api.entity.ProductsEntity;
import com.sankhya.felipebs.api.exception.custom.EntityNotFoundException;
import com.sankhya.felipebs.api.exception.custom.InsufficientQuantityProductException;
import com.sankhya.felipebs.api.repository.IOrderItemsRepository;
import com.sankhya.felipebs.api.repository.IOrdersRepository;
import com.sankhya.felipebs.api.repository.IProductsRepository;
import com.sankhya.felipebs.api.service.OrdersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes Unitários - Order Services")
public class OrdersServiceTest {

    @Mock
    private  IProductsRepository productsRepository;
    @Mock
    private IOrdersRepository ordersRepository;
    @Mock
    private IOrderItemsRepository orderItemsRepository;

    @InjectMocks
    private OrdersService ordersService;

    private OrderItemsDto orderItemsDto;
    private List<ProductsDto> productsDtoList;

    private List<ProductsEntity> productsEntityList;

    @BeforeEach
    void setUp() {
        productsEntityList = new ArrayList<>();
        productsDtoList = new ArrayList<>();
    }

    @Test
    @DisplayName("Deve lançar uma exceção ao tentar achar um produto inexistente")
    void deveLancarExcecaoComProdutoInexistente() {
        productsDtoList.add(new ProductsDto(5, 50));
        orderItemsDto = new OrderItemsDto(productsDtoList);

        when(productsRepository.findById(5L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            ordersService.createOrder(orderItemsDto);
        });
    }

    @Test
    @DisplayName("Deve lançar uma exceção ao criar pedido quando estoque é insuficiente")
    void deveLancarExcecaoComEstoqueInsuficiente() {
        productsDtoList.add(new ProductsDto(1, 2));
        productsDtoList.add(new ProductsDto(3, 2));

        orderItemsDto = new OrderItemsDto(productsDtoList);

        productsEntityList.add(new ProductsEntity(1L, "Item com estoque", 47.54, 5, true, 1));
        productsEntityList.add(new ProductsEntity(2L, "Item sem estoque", 32.99, 1, true, 1));

        when(productsRepository.findById(1L)).thenReturn(Optional.ofNullable(productsEntityList.get(0)));
        when(productsRepository.findById(3L)).thenReturn(Optional.ofNullable(productsEntityList.get(1)));

        assertThrows(InsufficientQuantityProductException.class, () -> {
            ordersService.createOrder(orderItemsDto);
        });
    }
}
