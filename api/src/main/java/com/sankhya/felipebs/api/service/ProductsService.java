package com.sankhya.felipebs.api.service;

import com.sankhya.felipebs.api.entity.ProductsEntity;
import com.sankhya.felipebs.api.repository.IProductsRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class ProductsService {
    private final IProductsRepository productsRepository;

    public ProductsService(IProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }

    public Page<ProductsEntity> searchByPagination(String search, int size, int page) {
        if(search == null || search.isEmpty()) { search = "%%"; }
        else { search = "%" + search.toLowerCase() + "%"; }

        return productsRepository.searchByPagination(search, PageRequest.of(page, size));
    }
}
