package com.viettridao.cafe.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//Đánh dấu đây là một lớp có thể được "nhúng" vào một entity khác để làm khóa chính phức hợp.
//Lớp này sẽ được dùng với annotation @EmbeddedId trong entity `ReservationEntity`.
@Embeddable
public class ReservationKey {

	// Đại diện cho ID của bàn được đặt.
	// Sẽ ánh xạ với cột `table_id` trong cơ sở dữ liệu.
	@Column(name = "table_id")
	private Integer idTable;

	// Đại diện cho ID của nhân viên tạo hoặc quản lý đặt bàn.
	@Column(name = "employee_id")
	private Integer idEmployee;

	// Đại diện cho ID của hóa đơn liên quan đến đặt bàn (nếu có).
	@Column(name = "invoice_id")
	private Integer idInvoice;
}
