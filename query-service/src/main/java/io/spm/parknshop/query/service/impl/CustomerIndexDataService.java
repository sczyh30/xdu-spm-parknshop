package io.spm.parknshop.query.service.impl;

import io.spm.parknshop.advertisement.domain.Advertisement;
import io.spm.parknshop.advertisement.repository.AdvertisementRepository;
import io.spm.parknshop.category.domain.Category;
import io.spm.parknshop.category.repository.CategoryRepository;
import io.spm.parknshop.product.domain.ProductVO;
import io.spm.parknshop.product.repository.ProductQueryRepository;
import io.spm.parknshop.query.vo.IndexPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.List;

import static io.spm.parknshop.common.async.ReactorAsyncWrapper.*;

/**
 * @author Eric Zhao
 */
@Service
public class CustomerIndexDataService {

  @Autowired
  private CategoryRepository categoryRepository;
  @Autowired
  private ProductQueryRepository productQueryRepository;
  @Autowired
  private AdvertisementRepository advertisementRepository;

  public Mono<IndexPage> renderCustomerIndex() {
    return async(this::retrieveInternal);
  }

  @Transactional(readOnly = true)
  protected IndexPage retrieveInternal() {
    List<Category> categories = categoryRepository.findAll();
    List<ProductVO> products = productQueryRepository.getNRecentProductVO(50);
    List<Advertisement> productAdList = advertisementRepository.getPresentProductAd();
    List<Advertisement> shopAdList = advertisementRepository.getPresentShopAd();
    return new IndexPage().setCategories(categories).setProductAdList(productAdList)
      .setProducts(products).setShopAdList(shopAdList);
  }
}
