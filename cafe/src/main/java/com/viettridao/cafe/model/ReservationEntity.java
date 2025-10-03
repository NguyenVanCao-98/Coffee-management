package com.viettridao.cafe.model;

import java.time.LocalDate;
import java.time.LocalTime;

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
@Table(name = "table_reservations_detail") // chitietdatban
public class ReservationEntity {
	@EmbeddedId
	private ReservationKey id;
	// Sử dụng khóa chính phức hợp (composite key), được định nghĩa trong lớp `ReservationKey`.
	// Gồm các khóa ngoại như ID bàn, ID nhân viên, ID hóa đơn.

	@ManyToOne
	@MapsId("idTable")
	@JoinColumn(name = "table_id")
	private TableEntity table;
	// Thiết lập quan hệ nhiều-1 với bảng `table`.
	// `@MapsId("idTable")`: ánh xạ phần id tương ứng trong `ReservationKey`
	// `@JoinColumn`: ánh xạ với cột `table_id` trong DB.

	@ManyToOne
	@MapsId("idEmployee")
	@JoinColumn(name = "employee_id")
	private EmployeeEntity employee;
	// Thiết lập quan hệ nhiều-1 với bảng `employee`.
	// `@MapsId("idEmployee")`: ánh xạ phần id tương ứng trong `ReservationKey`.

	@ManyToOne
	@MapsId("idInvoice")
	@JoinColumn(name = "invoice_id")
	private InvoiceEntity invoice;
	// Thiết lập quan hệ nhiều-1 với bảng `invoice` (hóa đơn).
	// `@MapsId("idInvoice")`: ánh xạ phần id tương ứng trong `ReservationKey`.

	@Column(name = "customer_name")
	private String customerName;
	// Tên của khách hàng đã đặt bàn.

	@Column(name = "reservation_time")
	private LocalTime reservationTime;
	// Thời gian khách muốn đến (ví dụ: 18:30).

	@Column(name = "customer_phone_number")
	private String customerPhone;
	// Số điện thoại của khách hàng.

	@Column(name = "reservation_datetime")
	private LocalDate reservationDate;
	// Ngày khách đặt bàn.

	@Column(name = "is_deleted")
	private Boolean isDeleted;
	// Cờ xóa mềm: true nếu đặt chỗ đã bị xóa, false nếu vẫn còn hiệu lực.

}
