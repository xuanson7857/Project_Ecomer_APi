package com.ra.service.Impl;

import com.ra.model.dto.request.ProductRequest;
import com.ra.model.dto.response.ProductDto;
import com.ra.model.entity.M5Category;
import com.ra.model.entity.M5OrderDetail;
import com.ra.model.entity.M5Product;
import com.ra.repository.*;
import com.ra.service.ProductService;
import com.ra.service.UploadService;
import com.ra.util.exception.BadRequestException;
import com.ra.util.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    UploadService uploadService;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderDetailRepository orderDetailRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository,
                              CategoryRepository categoryRepository,
                              CartItemRepository cartItemRepository,
                              OrderDetailRepository orderDetailRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.cartItemRepository = cartItemRepository;
        this.orderDetailRepository = orderDetailRepository;
    }

    @Override
    public Page<ProductDto> productByNameOrDes(String s, Pageable pageable) {
        Page<M5Product> products = productRepository.findAllByProductNameContainingOrDescriptionContaining(s, s, pageable);
        List<ProductDto> list = new ArrayList<>();
        for (M5Product product : products) {
            list.add(productDto(product));
        }
        return new PageImpl<>(list, pageable, list.size());
    }

    @Override
    public Page<ProductDto> sellingProduct(String sort, String col, Pageable pageable) {
        if (col != null) {
            if (sort.equals("des")) {
                pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(col).descending());
            }
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(col).ascending());

        }
        Page<M5Product> products = productRepository.findAllByCategoryStatus(true, pageable);
        List<ProductDto> list = new ArrayList<>();
        for (M5Product product : products) {
            if (product.getStockQuantity() > 0) {
                list.add(productDto(product));
            }
        }
        return new PageImpl<>(list, pageable, list.size());
    }

    @Override
    public Page<ProductDto> productByCategory(Long categoryId, Pageable pageable) throws NotFoundException {
        if (categoryRepository.findById(categoryId).orElse(null) == null) {
            throw new NotFoundException("Not found category with Id " + categoryId);
        }
        Page<M5Product> products = productRepository.findAllByCategoryCategoryId(categoryId, pageable);
        List<ProductDto> list = new ArrayList<>();
        for (M5Product product : products) {
            list.add(productDto(product));
        }
        return new PageImpl<>(list, pageable, list.size());
    }

    @Override
    public ProductDto singleProduct(Long id) throws NotFoundException {
        M5Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            throw new NotFoundException("Not found product with id " + id);
        }
        return productDto(product);
    }

    // admin
    @Override
    public Page<M5Product> products(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public M5Product productById(Long id) throws NotFoundException {
        M5Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            throw new NotFoundException("Not found product with ID " + id);
        }
        return product;
    }

    // admin
    @Override
    public M5Product save(ProductRequest product) throws NotFoundException {
        M5Category category = categoryRepository.findByCategoryName(product.getCategoryName()).orElse(null);
        if (category != null) {
            Date date = new Date(new java.util.Date().getTime());
            String fileName = uploadService.uploadImage(product.getImage());


            M5Product saveProduct = M5Product.builder()
                    .productName(product.getProductName())
                    .unitPrice(product.getUnitPrice())
                    .stockQuantity(product.getStockQuantity())
                    .category(category)
                    .description(product.getDescription())
                    .image(fileName)
                    .build();
            if (product.getProductId() != null) {
                M5Product found = productRepository.findById(product.getProductId()).orElse(null);
                saveProduct.setProductId(product.getProductId());
                saveProduct.setUpdateAt(date);
                if (found != null) {
                    saveProduct.setCreateAt(found.getCreateAt());
                } else {
                    saveProduct.setCreateAt(date);
                }
            } else {
                saveProduct.setCreateAt(date);
            }
            return productRepository.save(saveProduct);
        }
        throw new NotFoundException("Not found category " + product.getCategoryName());
    }

    @Override
    @Transactional
    public void delete(Long id) throws NotFoundException, BadRequestException {
        M5Product product = productById(id);
        if (isSafeToDelete(product)) {
            cartItemRepository.deleteAllByProduct(product);
            productRepository.delete(product);
        } else {

            throw new BadRequestException("Can't delete product with ID " + id);
        }
    }

    private ProductDto productDto(M5Product product) {
        return ProductDto.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .description(product.getDescription())
                .unitPrice(product.getUnitPrice())
                .image(product.getImage())
                .stockQuantity(product.getStockQuantity())
                .categoryName(product.getCategory().getCategoryName())
                .build();
    }

    private boolean isSafeToDelete(M5Product product) {
        List<M5OrderDetail> orderDetails = orderDetailRepository.findAllByProduct(product);
        for (M5OrderDetail orderDetail : orderDetails) {
            if (orderDetail.getProduct().equals(product)) {
                return false;
            }
        }
        return true;
    }
}
