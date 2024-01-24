package com.ra.controller;

import com.ra.model.dto.request.ProductRequest;
import com.ra.model.entity.M5Category;
import com.ra.model.entity.M5Product;
import com.ra.repository.RoleRepository;
import com.ra.service.CategoryService;
import com.ra.service.OrderService;
import com.ra.service.ProductService;
import com.ra.service.UserService;
import com.ra.util.exception.BadRequestException;
import com.ra.util.exception.NotFoundException;
import com.ra.util.exception.UnAuthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api.myservice.com/v1/admin")
public class AdminController {
    private final CategoryService categoryService;
    private final ProductService productService;
    private final UserService userService;
    private final OrderService orderService;
    private final RoleRepository roleRepository;

    @Autowired
    public AdminController(ProductService productService
            , CategoryService categoryService
            , UserService userService
            , OrderService orderService
            , RoleRepository roleRepository) {
        this.categoryService = categoryService;
        this.productService = productService;
        this.userService = userService;
        this.orderService = orderService;
        this.roleRepository = roleRepository;
    }

    //    user
    @GetMapping("/users")
    public ResponseEntity<?> usersList(@PageableDefault(size = 5, page = 0) Pageable pageable,
                                       @RequestParam(defaultValue = "asc", name = "sort") String sort,
                                       @RequestParam(defaultValue = "username", name = "col") String col) {
        return new ResponseEntity<>(userService.findAll(pageable, sort, col), HttpStatus.OK);
    }

    @PostMapping("/users/{userId}/role")
    public ResponseEntity<?> addUserRole() {
        return null;
    }

    @DeleteMapping("/users/{userId}/role")
    public ResponseEntity<?> deleteUserRole() {
        return null;
    }

    @PutMapping("/users/{userId}")
    public ResponseEntity<?> updateUserStatus(@PathVariable("userId") Long userId) throws NotFoundException, UnAuthorizedException {

        return new ResponseEntity<>(userService.updateUserStatus(userId), HttpStatus.OK);
    }

    @GetMapping("/roles")
    public ResponseEntity<?> roleList() {
        return new ResponseEntity<>(roleRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/users/search")
    public ResponseEntity<?> userSearch(@RequestParam(value = "", name = "search") String keyword,
                                        @PageableDefault(size = 5, page = 0) Pageable pageable,
                                        @RequestParam(defaultValue = "asc", name = "sort") String sort,
                                        @RequestParam(defaultValue = "username", name = "col") String col) {
        return new ResponseEntity<>(userService.findAllByUserName(pageable, sort, col, keyword), HttpStatus.OK);
    }

    //    product
    @GetMapping("/products")
    public ResponseEntity<?> productList(@PageableDefault(size = 5, page = 0) Pageable pageable) {

        return new ResponseEntity<>(productService.products(pageable), HttpStatus.OK);
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<?> productById(@PathVariable("productId") Long id) throws NotFoundException {
        M5Product product = productService.productById(id);
        if (product != null) {
            return new ResponseEntity<>(product, HttpStatus.OK);
        }
        return new ResponseEntity<>("Not found product with id " + id, HttpStatus.NOT_FOUND);
    }

    @PostMapping("/products")
    public ResponseEntity<?> addProduct(@ModelAttribute ProductRequest product) throws NotFoundException {
        M5Product newProduct = productService.save(product);
            return new ResponseEntity<>(newProduct, HttpStatus.OK);

    }

    @PutMapping("/products/{productId}")
    public ResponseEntity<?> editProduct(@PathVariable("productId") Long id,
                                         @ModelAttribute ProductRequest product) throws NotFoundException {
        M5Product found = productService.productById(id);
        if (found != null) {
            product.setProductId(id);
        }
        return new ResponseEntity<>(productService.save(product), HttpStatus.OK);
    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable("productId") Long id) throws NotFoundException, BadRequestException {
        productService.delete(id);
        return new ResponseEntity<>("delete success",HttpStatus.OK);
    }

    //    category
    @GetMapping("/categories")
    public ResponseEntity<?> categoryList() {
        return new ResponseEntity<>(categoryService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<?> categoryById(@PathVariable("categoryId") Long categoryId) {
        return new ResponseEntity<>(categoryService.categoryById(categoryId), HttpStatus.OK);
    }

    @PostMapping("/categories")
    public ResponseEntity<?> addCategory(@RequestBody M5Category category) throws BadRequestException {
        return new ResponseEntity<>(categoryService.save(category), HttpStatus.OK);
    }

    @PutMapping("/categories/{categoryId}")
    public ResponseEntity<?> editCategory(@RequestBody M5Category category, @PathVariable("categoryId") Long cateId) throws NotFoundException, BadRequestException {
        M5Category found = categoryService.findById(cateId);
        category.setCategoryId(found.getCategoryId());
        return new ResponseEntity<>(categoryService.save(category), HttpStatus.OK);
    }

    @DeleteMapping("/categories/{categoryId}")
    public ResponseEntity<?> deleteCategory(@PathVariable("categoryId") Long categoryId) throws BadRequestException {
        categoryService.deleteCategory(categoryId);
        return new ResponseEntity<>("delete successful", HttpStatus.OK);
    }

    // orders
    @GetMapping("/orders")
    public ResponseEntity<?> orderList(
            @PageableDefault(size = 5, page = 0) Pageable pageable,
            @RequestParam(name = "sort", defaultValue = "asc") String sort,
            @RequestParam(name = "col", defaultValue = "status") String col) {
        return new ResponseEntity<>(orderService.orderList(pageable, sort, col), HttpStatus.OK);
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<?> ordersById(@PathVariable("orderId") Long orderId) throws NotFoundException {

        return new ResponseEntity<>(orderService.findById(orderId), HttpStatus.OK);
    }

    @PutMapping("/orders/{orderId}/{status}")
    public ResponseEntity<?> updateOrderStatus(@PathVariable("orderId") Long id, @PathVariable("status") String status) throws NotFoundException {
        return new ResponseEntity<>(orderService.confirmOrder(id, status), HttpStatus.OK);
    }

    @GetMapping("/orders/status")
    public ResponseEntity<?> ordersByStatus(@RequestParam(defaultValue = "", name = "status") String status) throws BadRequestException {

        return new ResponseEntity<>(orderService.orderByStatus(status), HttpStatus.OK);
    }

    // dashboard
    @GetMapping("/dash-board/sales")
    public ResponseEntity<?> dashboardSales() {
        return null;
    }

    @GetMapping("/dash-board/sales/best-seller-products")
    public ResponseEntity<?> bestSalesProducts() {
        return null;
    }

    @GetMapping("/dash-board/sales/categories")
    public ResponseEntity<?> bestSalesCategory() {
        return null;
    }
}
