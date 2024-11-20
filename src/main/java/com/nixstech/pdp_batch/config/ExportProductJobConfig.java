package com.nixstech.pdp_batch.config;

import com.nixstech.pdp_batch.entity.Product;
import com.nixstech.pdp_batch.repository.ProductRepository;
import com.nixstech.pdp_batch.writer.ExportProductWriter;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.transaction.PlatformTransactionManager;

@Profile(value = "exportProduct")
@Configuration
@Slf4j
public class ExportProductJobConfig {

  @Value("${batch.page.basic-chunk-size}")
  private int basicChunkSize;

  @Value("${batch.export-product.export-file-path}")
  private String exportFilePath;

  @Bean
  public Job exportProductJob(JobRepository jobRepository, Step exportProductStep) {
    return new JobBuilder("exportProductJob", jobRepository)
        .incrementer(new RunIdIncrementer())
        .start(exportProductStep)
        .build();
  }

  @Bean
  public Step exportProductStep(JobRepository jobRepository,
      PlatformTransactionManager transactionManager,
      ItemReader<Product> itemReader,
      FlatFileItemWriter<Product> itemWriter) {
    return new StepBuilder("exportProductStep", jobRepository)
        .<Product, Product>chunk(basicChunkSize, transactionManager)
        .reader(itemReader)
        .writer(itemWriter)
        .allowStartIfComplete(true)
        .build();
  }

  @Bean
  public ItemReader<Product> itemReader(ProductRepository repository) {
    return new RepositoryItemReaderBuilder<Product>()
        .repository(repository)
        .sorts(Map.of("id", Direction.ASC))
        .pageSize(basicChunkSize)
        .methodName("findAll")
        .name("Export Product Reader")
        .build();
  }

  @Bean
  public FlatFileItemWriter<Product> itemWriter() {
    return new ExportProductWriter(exportFilePath);
  }
}
