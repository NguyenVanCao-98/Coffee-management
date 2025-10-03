package com.viettridao.cafe.model;

import java.time.LocalDate;
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
@Table(name = "promotions") // khuyenmai
public class PromotionEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "promotion_id")
	private Integer id;

	@Column(name = "promotion_name")
	private String promotionName;

	@Column(name = "start_date")
	private LocalDate startDate;

	@Column(name = "end_date")
	private LocalDate endDate;

	@Column(name = "discount_value")
	private Double discountValue;

	@Column(name = "is_deleted")
	private Boolean isDeleted;

	@OneToMany(mappedBy = "promotion", cascade = CascadeType.ALL)
	private List<InvoiceEntity> invoices;
}
