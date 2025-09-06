package com.sankhya.felipebs.api.repository;

import com.sankhya.felipebs.api.entity.ProductsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface IProductsRepository extends JpaRepository<ProductsEntity, Long> {

    @Query("FROM ProductsEntity WHERE name like :search")
    Page<ProductsEntity> searchByPagination(String search, Pageable of);

    @Query("FROM ProductsEntity WHERE id = :id")
    Optional<ProductsEntity> findById(long id);
}
