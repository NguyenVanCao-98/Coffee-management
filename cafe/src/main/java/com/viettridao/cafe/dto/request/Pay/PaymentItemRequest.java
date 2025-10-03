package com.viettridao.cafe.dto.request.Pay;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//PaymentItemRequest chứa thông tin món cần thanh toán – gồm ID món và số lượng
public class PaymentItemRequest {

	@NotNull(message = "ID món không được để trống")
	// Ràng buộc: `menuItemId` không được null. Nếu không nhập sẽ báo lỗi như trên.
	private Integer menuItemId;
	// Biến lưu ID của món ăn trong hóa đơn.


	@NotNull(message = "Số lượng không được để trống")
	// Ràng buộc: `quantity` không được để trống (null).
	@Min(value = 1, message = "Số lượng phải lớn hơn 0")
	// Ràng buộc: Số lượng phải ≥ 1 (không được âm hoặc bằng 0).
	private Integer quantity;
	// Biến lưu số lượng món đã chọn.


	@NotNull(message = "Giá không được để trống")
	// Ràng buộc: `price` không được null.
	@Positive(message = "Giá phải lớn hơn 0")
	// Ràng buộc: Giá phải > 0 (không được bằng 0 hay âm).
	private Double price;
	// Biến lưu đơn giá của món.


	@NotNull(message = "Thành tiền không được để trống")
	// Ràng buộc: `amount` không được null.
	@PositiveOrZero(message = "Thành tiền phải lớn hơn hoặc bằng 0")
	// Ràng buộc: Thành tiền phải ≥ 0 (có thể bằng 0 nếu miễn phí, khuyến mãi...).
	private Double amount;
	// Biến lưu thành tiền (tổng giá = số lượng × đơn giá).

}
