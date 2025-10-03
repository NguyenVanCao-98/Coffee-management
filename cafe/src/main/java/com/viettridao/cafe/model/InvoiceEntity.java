package com.viettridao.cafe.model;

import java.time.LocalDateTime;
import java.util.List;

import com.viettridao.cafe.common.InvoiceStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "invoices") // hoadon
public class InvoiceEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "invoice_id")
	private Integer id;
	// Khóa chính của bảng hóa đơn, tự động tăng.

	@Column(name = "total_amount")
	private Double totalAmount;
	// Tổng số tiền của hóa đơn.

	@Column(name = "created_at")
	private LocalDateTime createdAt;
	// Thời điểm tạo hóa đơn.

	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private InvoiceStatus status;
	// Trạng thái của hóa đơn, ví dụ: PAID, UNPAID, CANCELED.
	// Dữ liệu được lưu dưới dạng chuỗi trong DB.

	@Column(name = "is_deleted")
	private Boolean isDeleted;
	// Cờ đánh dấu hóa đơn đã bị xóa mềm (soft delete) hay chưa.

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "promotion_id")
	private PromotionEntity promotion;
	// Ánh xạ quan hệ nhiều hóa đơn - một khuyến mãi.
	// Mỗi hóa đơn có thể áp dụng một khuyến mãi.

	@OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL)
	private List<InvoiceDetailEntity> invoiceDetails;
	// Danh sách các chi tiết hóa đơn (các món đã gọi).
	// mappedBy = "invoice" nghĩa là quan hệ này được ánh xạ ngược từ thuộc tính `invoice` trong `InvoiceDetailEntity`.

	@OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL)
	private List<ReservationEntity> reservations;
	// Danh sách các đặt bàn liên quan đến hóa đơn này.

}
