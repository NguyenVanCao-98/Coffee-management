package com.viettridao.cafe.model;

import java.time.LocalDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "imports") // donnhap
public class ImportEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "imports_id")
	private Integer id;

	@Column(name = "import_date")
	private LocalDate importDate;

	@Column(name = "total_amount")
	private Double totalAmount;

	@Column(name = "price")
	private Double price;

	@Column(name = "quantity")
	private Integer quantity;

	@Column(name = "is_deleted")
	private Boolean isDeleted;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "employee_id")
	private EmployeeEntity employee;

	@ManyToOne
	@JoinColumn(name = "product_id")
	private ProductEntity product;

}
