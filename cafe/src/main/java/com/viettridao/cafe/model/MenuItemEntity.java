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
@Table(name = "menu_items") // thucdon
public class MenuItemEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "menu_item_id")
	private Integer id;

	@Column(name = "item_name")
	private String itemName;

	@Column(name = "current_price")
	private Double currentPrice;

	@Column(name = "is_deleted")
	private Boolean isDeleted;

	@OneToMany(mappedBy = "menuItem", cascade = CascadeType.ALL)
	private List<MenuDetailEntity> menuDetails;

	@OneToMany(mappedBy = "menuItem", cascade = CascadeType.ALL)
	private List<InvoiceDetailEntity> invoiceDetails;
}
