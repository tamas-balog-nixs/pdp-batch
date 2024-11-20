package com.nixstech.pdp_batch.repository;

import com.nixstech.pdp_batch.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

}