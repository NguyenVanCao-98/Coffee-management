package com.viettridao.cafe.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class InvoiceKey {
	@Column(name = "invoice_id")
	private Integer idInvoice;
	// Trường lưu mã hóa đơn, ánh xạ với cột `invoice_id` trong cơ sở dữ liệu.
	// Đây là một phần của khóa chính tổng hợp, dùng để xác định hóa đơn nào chứa món này.

	@Column(name = "menu_item_id")
	private Integer idMenuItem;
	// Trường lưu mã món, ánh xạ với cột `menu_item_id` trong cơ sở dữ liệu.
	// Đây là phần còn lại của khóa chính tổng hợp, xác định món nào thuộc hóa đơn này.

}
