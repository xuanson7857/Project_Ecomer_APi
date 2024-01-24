package com.ra.service;

import com.ra.model.dto.request.ProductRequest;
import com.ra.model.dto.response.ProductDto;
import com.ra.model.entity.M5Product;
import com.ra.util.exception.BadRequestException;
import com.ra.util.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    Page<ProductDto> productByNameOrDes(String s,Pageable pageable);
    Page<ProductDto> sellingProduct(String sort, String col, Pageable pageable);
    Page<ProductDto> productByCategory(Long categoryId,Pageable pageable) throws NotFoundException;
    ProductDto singleProduct(Long id) throws NotFoundException;
    Page<M5Product> products(Pageable pageable);
    M5Product productById(Long id) throws NotFoundException;
    M5Product save(ProductRequest product) throws NotFoundException;
    void delete(Long id) throws NotFoundException, BadRequestException;
}
