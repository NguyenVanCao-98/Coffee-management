package com.viettridao.cafe.dto.request.Pay;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequest {

	@NotNull(message = "ID bàn không được để trống")
	// Ràng buộc: `tableId` không được null. Hệ thống cần ID bàn để xác định hóa đơn tương ứng.
	private Integer tableId;
	// Biến lưu ID của bàn đang thanh toán.


	@NotNull(message = "Tiền khách đưa không được để trống")
	// Ràng buộc: `customerCash` không được null (phải nhập tiền khách thanh toán).
	@PositiveOrZero(message = "Tiền khách đưa phải lớn hơn hoặc bằng 0")
	// Ràng buộc: Số tiền khách đưa phải ≥ 0 (cho phép bằng 0 nếu khách nợ hoặc thanh toán sau).
	private Integer customerCash;
	// Biến lưu số tiền khách đưa khi thanh toán.


	@NotNull(message = "Trạng thái bàn không được để trống")
	// Ràng buộc: `freeTable` không được null. Dùng để xác định sau khi thanh toán thì bàn có được chuyển về trạng thái trống hay không.
	private Boolean freeTable;
	// Biến lưu trạng thái bàn sau khi thanh toán: `true` nếu bàn được giải phóng, `false` nếu tiếp tục sử dụng.


	@NotNull(message = "Danh sách món thanh toán không được để trống")
	// Ràng buộc: danh sách món phải được cung cấp khi thanh toán.
	@Valid
	// Áp dụng xác thực cho từng phần tử trong danh sách `items` (mỗi phần tử là `PaymentItemRequest`).
	private List<PaymentItemRequest> items;
	// Danh sách các món được thanh toán (gồm thông tin: món, số lượng, đơn giá, thành tiền...).

}
