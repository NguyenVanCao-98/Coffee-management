package com.viettridao.cafe.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "units") // donvitinh
public class UnitEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "unit_id")
	private Integer id;

	@Column(name = "unit_name")
	private String unitName;

	@Column(name = "is_deleted")
	private Boolean isDeleted;

	@OneToMany(mappedBy = "unit", cascade = CascadeType.ALL)
	private List<ProductEntity> products;
}
