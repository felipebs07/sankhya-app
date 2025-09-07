package com.sankhya.felipebs.api.controller;

import com.sankhya.felipebs.api.entity.ProductsEntity;
import com.sankhya.felipebs.api.service.ProductsService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/products")
public class ProductsController {

    private final ProductsService productsService;
    public ProductsController(ProductsService productsService) {
        this.productsService = productsService;
    }

    @GetMapping
    public ResponseEntity<Page<ProductsEntity>> searchByPagination(
            @RequestParam(name = "search", defaultValue = "%%") String search,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "page", defaultValue = "1") int page) {
        var content = productsService.searchByPagination(search, size, page);
        if(content.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(content);
    }
}
