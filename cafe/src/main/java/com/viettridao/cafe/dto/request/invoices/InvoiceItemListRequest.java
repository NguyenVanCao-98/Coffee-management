package com.viettridao.cafe.dto.request.invoices;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//InvoiceItemListRequest chứa danh sách nhiều món cùng lúc để thêm vào hóa đơn.
public class InvoiceItemListRequest {

	@NotEmpty(message = "Danh sách món không được để trống")
	// Ràng buộc: danh sách `items` không được rỗng. Phải chọn ít nhất một món khi
	// thêm vào hóa đơn.
	@Valid
	// Đảm bảo các phần tử trong danh sách `items` cũng được kiểm tra hợp lệ theo
	// quy tắc trong lớp `InvoiceItemRequest`.
	private List<InvoiceItemRequest> items;
	// Danh sách các món được thêm vào hóa đơn. Mỗi phần tử đại diện cho một món kèm
	// số lượng.

}
