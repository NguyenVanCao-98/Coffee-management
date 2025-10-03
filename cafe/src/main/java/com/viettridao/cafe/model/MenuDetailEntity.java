package com.viettridao.cafe.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "menu_item_ingredients")
public class MenuDetailEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "product_id")
	private ProductEntity product;

	@ManyToOne
	@JoinColumn(name = "menu_item_id")
	private MenuItemEntity menuItem;


	@Column(name = "quantity")
	private Double quantity;

	@Column(name = "unit")
	private String unitName;

	@Column(name = "is_deleted")
	private Boolean isDeleted;
}
