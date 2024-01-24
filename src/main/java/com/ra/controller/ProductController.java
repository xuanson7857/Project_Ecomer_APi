package com.ra.controller;

import com.ra.model.dto.response.ProductDto;
import com.ra.service.ProductService;
import com.ra.util.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api.myservice.com/v1")
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/product/search")
    public ResponseEntity<?> productSearch(@RequestParam(defaultValue = "",name = "search") String search
            , @PageableDefault(size = 5, page = 0) Pageable pageable) {

        return new ResponseEntity<>(productService.productByNameOrDes(search, pageable), HttpStatus.OK);
    }

    @GetMapping("/products")
    public ResponseEntity<?> availableProduct(
            @RequestParam(required = false, name = "sort", defaultValue = "") String sort,
            @RequestParam(required = false, name = "type", defaultValue = "productName") String col,
            @PageableDefault(size = 5, page = 0) Pageable pageable) {
        return new ResponseEntity<>(productService.sellingProduct(sort, col, pageable), HttpStatus.OK);
    }

    @GetMapping("/products/featured-products")
    public ResponseEntity<?> featuredProduct() {
        return null;
    }

    @GetMapping("/products/new-products")
    public ResponseEntity<?> newProduct() {
        Pageable pageable = PageRequest.of(0, 5);
        return new ResponseEntity<>(productService.sellingProduct("des", "createAt", pageable), HttpStatus.OK);
    }

    @GetMapping("/products/bets-seller-products")
    public ResponseEntity<?> bestSellerProduct() {
        return null;
    }

    @GetMapping("/products/categories/{categoryId}")
    public ResponseEntity<?> productsByCate(@PathVariable("categoryId") Long categoryId,
                                            @PageableDefault(size = 5, page = 0) Pageable pageable) throws NotFoundException {

        return new ResponseEntity<>(productService.productByCategory(categoryId, pageable), HttpStatus.OK);
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<?> productById(@PathVariable("productId") Long productId) throws NotFoundException {
        return new ResponseEntity<>(productService.singleProduct(productId), HttpStatus.OK);
    }
}
