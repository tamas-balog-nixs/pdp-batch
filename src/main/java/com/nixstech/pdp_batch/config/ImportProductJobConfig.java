package com.nixstech.pdp_batch.config;

import com.nixstech.pdp_batch.entity.Product;
import com.nixstech.pdp_batch.processor.ImportProductProcessor;
import com.nixstech.pdp_batch.reader.ImportProductReader;
import com.nixstech.pdp_batch.repository.CategoryRepository;
import com.nixstech.pdp_batch.repository.ProductRepository;
import com.nixstech.pdp_batch.request.ImportProductRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.PlatformTransactionManager;

@Profile(value = "importProduct")
@Configuration
@Slf4j
public class ImportProductJobConfig {

  @Value("${batch.page.basic-chunk-size}")
  private int basicChunkSize;

  @Value("${batch.import-product.input-file-path}")
  private String inputFilePath;

  @Bean
  public Job importProductJob(JobRepository jobRepository,
      Step importProductStep) {
    return new JobBuilder("importProductJob", jobRepository)
        .incrementer(new RunIdIncrementer())
        .start(importProductStep)
        .build();
  }

  @Bean
  public Step importProductStep(JobRepository jobRepository,
      PlatformTransactionManager transactionManager,
      ItemReader<ImportProductRequest> itemReader,
      ItemProcessor<ImportProductRequest, Product> processor) {
    return new StepBuilder("importProductStep", jobRepository)
        .<ImportProductRequest, Product>chunk(basicChunkSize, transactionManager)
        .reader(itemReader)
        .processor(processor)
        .writer(
            chunk -> chunk.forEach(product -> log.info("added product: {}", product.getTitle())))
        .allowStartIfComplete(true)
        .build();
  }

  @Bean
  public ItemReader<ImportProductRequest> itemReader() {
    return new ImportProductReader(inputFilePath);
  }

  @Bean
  public ItemProcessor<ImportProductRequest, Product> itemProcessor(
      ProductRepository productRepository, CategoryRepository categoryRepository) {
    return new ImportProductProcessor(productRepository, categoryRepository);
  }
}
