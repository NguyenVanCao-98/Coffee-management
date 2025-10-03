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
@Table(name = "exports") // donxuat
public class ExportEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "exports_id")
	private Integer id;

	@Column(name = "total_export_amount")
	private Double totalExportAmount;

	@Column(name = "export_date")
	private LocalDate exportDate;

	@Column(name = "quantity")
	private Integer quantity;

	@Column(name = "is_deleted")
	private Boolean isDeleted;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "employee_id")
	private EmployeeEntity employee;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "product_id")
	private ProductEntity product;
}
