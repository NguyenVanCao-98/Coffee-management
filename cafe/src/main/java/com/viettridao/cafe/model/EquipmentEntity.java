package com.viettridao.cafe.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "equipment") // thietbi
public class EquipmentEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "equipment_id")
	private Integer id;

	@Column(name = "equipment_name")
	private String equipmentName;

	@Column(name = "quantity")
	private Integer quantity;

	@Column(name = "notes")
	private String notes;

	@Column(name = "purchase_date")
	private LocalDate purchaseDate;

	@Column(name = "purchase_price")
	private Double purchasePrice;

	@Column(name = "is_deleted")
	private Boolean isDeleted;
}
