package com.viettridao.cafe.dto.request.invoices;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//InvoiceItemRequest dùng khi thêm món vào hóa đơn – chứa ID món và số lượng.
public class InvoiceItemRequest {

	@NotNull(message = "Mã hóa đơn không được để trống")
	// Ràng buộc: `invoiceId` bắt buộc phải có (không được null). Hệ thống cần biết món này thuộc về hóa đơn nào.
	private Integer invoiceId;
	// Biến lưu ID của hóa đơn mà món này thuộc về.


	@NotNull(message = "Mã món không được để trống")
	// Ràng buộc: `menuItemId` bắt buộc phải có. Mỗi món ăn/đồ uống phải có mã định danh riêng.
	private Integer menuItemId;
	// Biến lưu ID của món được thêm vào hóa đơn.


	@NotNull(message = "Số lượng không được để trống")
	// Ràng buộc: `quantity` không được null. Phải nhập số lượng món muốn gọi.
	private Integer quantity;
	// Biến lưu số lượng món được gọi cho hóa đơn.

}
