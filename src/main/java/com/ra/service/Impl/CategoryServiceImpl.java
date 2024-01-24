package com.ra.service.Impl;

import com.ra.model.entity.M5Category;
import com.ra.model.entity.M5Product;
import com.ra.repository.CategoryRepository;
import com.ra.repository.ProductRepository;
import com.ra.service.CategoryService;
import com.ra.util.exception.BadRequestException;
import com.ra.util.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    @Override
    public List<M5Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public List<M5Category> findsAvailable() {
        return categoryRepository.findAllByStatus(true);
    }

    @Override
    public M5Category save(M5Category category) throws BadRequestException {
        if (!isUnique(category.getCategoryName(), category.getCategoryId())) {
            throw new BadRequestException(category.getCategoryName() + " category already exists");
        }
        return categoryRepository.save(category);
    }

    @Override
    public M5Category categoryById(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(() -> new RuntimeException("not found"));
    }

    @Override
    public void deleteCategory(Long id) throws BadRequestException {
        if (!isSafeToDelete(id)) {
            throw new BadRequestException("can't delete category with id " + id);
        }
        categoryRepository.deleteById(id);
    }

    @Override
    public M5Category findById(Long id) throws NotFoundException {
        M5Category category = categoryRepository.findById(id).orElse(null);
        if (category == null) {
            throw new NotFoundException("Not found category with ID " + id);
        }
        return category;
    }

    Boolean isSafeToDelete(Long cateId) {
        List<M5Product> products = productRepository.findAll();
        for (M5Product product : products) {
            if (product.getCategory().getCategoryId().equals(cateId)) {
                return false;
            }
        }
        return true;
    }

    Boolean isUnique(String name, Long id) {

        for (M5Category category : findAll()) {

            if (category.getCategoryName().equalsIgnoreCase(name) && !category.getCategoryId().equals(id) ) {
                return false;
            }
        }
        return true;
    }
}
