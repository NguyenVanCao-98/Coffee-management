package com.viettridao.cafe.dto.response.Pay;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
//Tự động tạo constructor với tất cả các trường (dùng Lombok)
@NoArgsConstructor
//Tự động tạo constructor không có tham số (dùng Lombok)
@Getter
@Setter
public class PaymentResponse {

	private boolean success;
	// Cờ báo kết quả thanh toán: true nếu thành công, false nếu thất bại

	private String message;
	// Thông báo kết quả thanh toán, ví dụ: "Thanh toán thành công", "Không đủ tiền"

	private Double totalAmount;
	// Tổng tiền cần thanh toán

	private Double customerCash;
	// Số tiền khách đã đưa

	private Double change;
	// Số tiền cần thối lại cho khách

	private Integer invoiceId;
	// ID của hóa đơn vừa được tạo hoặc xử lý

	private String invoiceStatus;
	// Trạng thái của hóa đơn, ví dụ: "Đã thanh toán", "Đã hủy"

	private String paidByName;
	// Tên nhân viên thực hiện thanh toán

	private Integer paidById;
	// ID của nhân viên thực hiện thanh toán

	private Integer tableId;
	// ID của bàn vừa được thanh toán

}
