package com.nixstech.pdp_batch.request;

import lombok.Data;

@Data
public class ImportProductRequest {

  String title;
  String alias;
  String description;
  int price;
  String image;
  String thumbnailImage;
  int categoryId;
}
