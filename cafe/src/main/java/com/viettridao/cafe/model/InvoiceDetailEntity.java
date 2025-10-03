package com.viettridao.cafe.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "invoice_details")
public class InvoiceDetailEntity {
	@EmbeddedId
	private InvoiceKey id;
	// Sử dụng khóa chính tổng hợp (composite key) được định nghĩa trong class InvoiceKey.

	@ManyToOne
	@MapsId("idInvoice")
	@JoinColumn(name = "invoice_id")
	private InvoiceEntity invoice;
	// Quan hệ nhiều-1 với bảng hóa đơn (Invoice).
	// Dùng @MapsId để ánh xạ một phần của khóa chính tổng hợp (idInvoice).

	@ManyToOne
	@MapsId("idMenuItem")
	@JoinColumn(name = "menu_item_id")
	private MenuItemEntity menuItem;
	// Quan hệ nhiều-1 với bảng món (MenuItem).
	// Dùng @MapsId để ánh xạ phần còn lại của khóa chính tổng hợp (idMenuItem).

	@Column(name = "quantity")
	private Integer quantity;
	// Số lượng món được gọi trong hóa đơn.

	@Column(name = "price_at_sale_time")
	private Double price;
	// Giá món tại thời điểm bán (có thể khác giá hiện tại trong menu).

	@Column(name = "is_deleted")
	private Boolean isDeleted;
	// Cờ xóa mềm, true nếu dòng này bị xóa logic.

}
