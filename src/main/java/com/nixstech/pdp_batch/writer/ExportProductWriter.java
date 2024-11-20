package com.nixstech.pdp_batch.writer;

import com.nixstech.pdp_batch.entity.Product;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.core.io.FileSystemResource;

public class ExportProductWriter extends FlatFileItemWriter<Product> {

  private static final String PRODUCT_EXPORT_FILE_HEADER =
      "title,alias,description,price,image,thumbnailImage,categoryId";

  public ExportProductWriter(String exportFilePath) {
    this.setResource(new FileSystemResource(exportFilePath));
    this.setLineAggregator(getProductDelimitedLineAggregator());
    this.setHeaderCallback(writer -> writer.write(PRODUCT_EXPORT_FILE_HEADER));
  }

  private static DelimitedLineAggregator<Product> getProductDelimitedLineAggregator() {
    DelimitedLineAggregator<Product> lineAggregator = new DelimitedLineAggregator<>();
    lineAggregator.setDelimiter(",");
    lineAggregator.setFieldExtractor(getFieldExtractor());
    return lineAggregator;
  }

  private static BeanWrapperFieldExtractor<Product> getFieldExtractor() {
    String[] fieldExtractorNames = new String[]{"title", "alias", "description", "price", "image",
        "thumbnailImage", "category.id"};
    BeanWrapperFieldExtractor<Product> fieldExtractor = new BeanWrapperFieldExtractor<>();
    fieldExtractor.setNames(fieldExtractorNames);
    return fieldExtractor;
  }
}
