package com.nixstech.pdp_batch.reader;

import com.nixstech.pdp_batch.request.ImportProductRequest;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.FileSystemResource;

public class ImportProductReader extends FlatFileItemReader<ImportProductRequest> {

  public ImportProductReader(String inputFilePath) {
    this.setResource(new FileSystemResource(inputFilePath));
    this.setLinesToSkip(1);
    this.setLineMapper(getLineMapper());
  }

  private static DefaultLineMapper<ImportProductRequest> getLineMapper() {
    DefaultLineMapper<ImportProductRequest> lineMapper = new DefaultLineMapper<>();
    lineMapper.setLineTokenizer(getTokenizer());
    lineMapper.setFieldSetMapper(getFieldSetMapper());
    return lineMapper;
  }

  private static DelimitedLineTokenizer getTokenizer() {
    DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
    delimitedLineTokenizer.setNames("title", "alias", "description", "price", "image",
        "thumbnailImage", "categoryId");
    return delimitedLineTokenizer;
  }

  private static BeanWrapperFieldSetMapper<ImportProductRequest> getFieldSetMapper() {
    BeanWrapperFieldSetMapper<ImportProductRequest> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
    fieldSetMapper.setTargetType(ImportProductRequest.class);
    return fieldSetMapper;
  }
}
