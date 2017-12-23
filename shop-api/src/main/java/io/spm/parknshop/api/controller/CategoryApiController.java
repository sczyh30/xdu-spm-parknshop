package io.spm.parknshop.api.controller;

import io.spm.parknshop.category.domain.Category;
import io.spm.parknshop.category.service.CategoryService;
import io.spm.parknshop.product.domain.ProductVO;
import io.spm.parknshop.product.service.ProductService;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/")
public class CategoryApiController {

  @Autowired
  private ProductService productService;
  @Autowired
  private CategoryService categoryService;

  @GetMapping("/catalog/{id}/products")
  public /*Mono<Map<String, ?>>*/ Mono<?> apiGetProductsByCategory(@PathVariable("id") Long id) {
    return categoryService.getById(id)
      .filter(Optional::isPresent)
      .map(Optional::get)
      .flatMap(catalog -> productService.getVOByCategoryId(id)
        .collectList()
        .map(products -> wrapCategoryProduct(catalog, products))
      );
  }

  private Map<String, Object> wrapCategoryProduct(Category category, List<ProductVO> products) {
    Map<String, Object> map = new HashMap<>(4);
    map.put("catalog", category);
    map.put("products", products);
    return map;
  }

  @GetMapping("/catalog/{id}")
  public Mono<Category> apiGetCategory(@PathVariable("id") Long id) {
    return categoryService.getById(id)
      .filter(Optional::isPresent)
      .map(Optional::get);
  }

  @GetMapping("/categories/all")
  public Publisher<Category> apiGetAll() {
    return categoryService.getAll();
  }

}
