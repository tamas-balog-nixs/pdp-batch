package com.nixstech.pdp_batch.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "category")
public class Category {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Integer id;

  @Column(name = "title", nullable = false, length = 155)
  private String title;

  @Column(name = "alias")
  private String alias;

  @Column(name = "image")
  private String imageURL;

  @Column(name = "enabled", nullable = false)
  private Boolean enabled;

  //parent_id refers to category id or null if it's top-level category
  @ManyToOne
  @JoinColumn(name = "parent_id")
  private Category parent;

  @OneToMany(mappedBy = "parent")
  @OrderBy("title asc")
  @ToString.Exclude
  private Set<Category> children = new HashSet<>();

  @OneToMany(mappedBy = "category", cascade = {CascadeType.ALL, CascadeType.PERSIST})
  @ToString.Exclude
  private List<Product> products;

  @Column(name = "all_parent_ids")
  @ToString.Exclude
  private String allParentsIDs;
}