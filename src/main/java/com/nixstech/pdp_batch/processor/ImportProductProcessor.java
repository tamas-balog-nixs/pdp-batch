package com.nixstech.pdp_batch.processor;

import com.nixstech.pdp_batch.entity.Category;
import com.nixstech.pdp_batch.entity.Product;
import com.nixstech.pdp_batch.repository.CategoryRepository;
import com.nixstech.pdp_batch.repository.ProductRepository;
import com.nixstech.pdp_batch.request.ImportProductRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;

@RequiredArgsConstructor
public class ImportProductProcessor implements ItemProcessor<ImportProductRequest, Product> {

  private final ProductRepository repository;
  private final CategoryRepository categoryRepository;

  @Override
  public Product process(ImportProductRequest request) {
    Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(
        () -> new IllegalArgumentException(
            "Cannot find catgory by id: " + request.getCategoryId()));

    return repository.save(Product.builder()
        .title(request.getTitle())
        .alias(request.getAlias())
        .description(request.getDescription())
        .description(request.getDescription())
        .price(request.getPrice())
        .image(request.getImage())
        .thumbnailImage(request.getThumbnailImage())
        .category(category)
        .build());
  }
}
