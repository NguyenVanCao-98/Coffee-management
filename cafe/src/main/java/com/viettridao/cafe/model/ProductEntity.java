package com.viettridao.cafe.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "products") // hanghoa
public class ProductEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "product_id")
	private Integer id;

	@Column(name = "product_name")
	private String productName;

	@Column(name = "quantity")
	private Integer quantity;


	@Column(name = "is_deleted")
	private Boolean isDeleted;

	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
	private List<ImportEntity> imports;

	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
	private List<ExportEntity> exports;

	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
	private List<MenuDetailEntity> menuDetails;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "unit_id", nullable = false)
	private UnitEntity unit;
}
