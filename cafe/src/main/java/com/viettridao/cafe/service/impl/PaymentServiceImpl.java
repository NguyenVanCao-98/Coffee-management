package com.viettridao.cafe.service.impl;

import java.util.Comparator;
import java.util.List;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.viettridao.cafe.common.InvoiceStatus;
import com.viettridao.cafe.common.TableStatus;
import com.viettridao.cafe.dto.request.Pay.PaymentRequest;
import com.viettridao.cafe.dto.response.Pay.PaymentResponse;
import com.viettridao.cafe.model.EmployeeEntity;
import com.viettridao.cafe.model.InvoiceDetailEntity;
import com.viettridao.cafe.model.InvoiceEntity;
import com.viettridao.cafe.model.ReservationEntity;
import com.viettridao.cafe.model.TableEntity;
import com.viettridao.cafe.repository.InvoiceItemDetailRepository;
import com.viettridao.cafe.repository.InvoiceRepository;
import com.viettridao.cafe.repository.ReservationRepository;
import com.viettridao.cafe.repository.TableRepository;
import com.viettridao.cafe.service.PaymentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

	private final InvoiceRepository invoiceRepository;
	private final InvoiceItemDetailRepository invoiceItemDetailRepository;
	private final TableRepository tableRepository;
	private final ReservationRepository reservationRepository;

	// Scheduler để chạy task delayed
	private final ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();

	{
		taskScheduler.initialize(); // Khởi tạo scheduler
	}

	@Transactional
	@Override
	public PaymentResponse processPayment(PaymentRequest request) {
		Integer tableId = request.getTableId();
		Integer customerCash = request.getCustomerCash();

		InvoiceEntity invoice = invoiceRepository
				.findTopByReservations_Table_IdAndStatusAndIsDeletedFalseOrderByCreatedAtDesc(tableId,
						InvoiceStatus.UNPAID);

		if (invoice == null) {
			return new PaymentResponse(false, "Không tìm thấy hóa đơn chưa thanh toán", null, null, null, null,
					"NOT_FOUND", null, null, tableId);
		}

		List<InvoiceDetailEntity> items = invoiceItemDetailRepository
				.findByInvoice_IdAndIsDeletedFalse(invoice.getId());

		if (items == null || items.isEmpty()) {
			return new PaymentResponse(false, "Hóa đơn không có món nào", 0.0,
					customerCash != null ? customerCash.doubleValue() : 0.0,
					customerCash != null ? customerCash.doubleValue() : 0.0, invoice.getId(),
					invoice.getStatus().name(), null, null, tableId);
		}

		double totalAmount = items.stream().mapToDouble(item -> item.getPrice() * item.getQuantity()).sum();

		if (customerCash < totalAmount) {
			return new PaymentResponse(false, "Số tiền khách đưa không đủ để thanh toán", totalAmount,
					customerCash != null ? customerCash.doubleValue() : 0.0, 0.0, // hoặc null nếu constructor chấp nhận
																					// Double
					invoice.getId(), invoice.getStatus().name(), null, null, tableId);
		}

		EmployeeEntity employee = invoice.getReservations().stream()
				.filter(r -> r.getIsDeleted() == null || !r.getIsDeleted())
				.max(Comparator.comparing(ReservationEntity::getReservationDate)
						.thenComparing(ReservationEntity::getReservationTime))
				.map(ReservationEntity::getEmployee).orElse(null);

		String paidByName = (employee != null) ? employee.getFullName() : "Không rõ";
		Integer paidById = (employee != null) ? employee.getId() : null;

		// Cập nhật hóa đơn
		invoice.setStatus(InvoiceStatus.PAID);
		invoice.setTotalAmount(totalAmount);
		invoiceRepository.save(invoice);

		List<ReservationEntity> reservations = reservationRepository.findByInvoice_IdAndIsDeletedFalse(invoice.getId());
		for (ReservationEntity r : reservations) {
			r.setIsDeleted(true);
		}
		reservationRepository.saveAll(reservations);

		// Giải phóng bàn
		if (Boolean.TRUE.equals(request.getFreeTable())) {
			// Checkbox chọn → trống ngay
			setTableAvailableNow(tableId);
		} else {
			// Checkbox bỏ chọn → trống sau 5 giây
			setTableAvailableDelayed(tableId, 5); // delay 5 giây
		}

		double change = customerCash - totalAmount;

		return new PaymentResponse(true, "Thanh toán thành công", totalAmount,
				customerCash != null ? customerCash.doubleValue() : 0.0, // ép Integer → Double
				change, // change là double, không cần null check
				invoice.getId(), invoice.getStatus().name(), paidByName, paidById, tableId);
	}

	/** Set bàn trống ngay lập tức */
	private void setTableAvailableNow(Integer tableId) {
		TableEntity table = tableRepository.findById(tableId)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy bàn ID: " + tableId));
		table.setStatus(TableStatus.AVAILABLE);
		tableRepository.save(table);
	}

	/** Set bàn trống sau delay (giây) */
	@Async
	private void setTableAvailableDelayed(Integer tableId, int delaySeconds) {
		taskScheduler.schedule(() -> {
			TableEntity table = tableRepository.findById(tableId)
					.orElseThrow(() -> new RuntimeException("Không tìm thấy bàn ID: " + tableId));
			table.setStatus(TableStatus.AVAILABLE);
			tableRepository.save(table);
			System.out.println("Bàn " + tableId + " đã tự động chuyển sang AVAILABLE sau " + delaySeconds + " giây.");
		}, java.util.Date.from(java.time.Instant.now().plusSeconds(delaySeconds)));
	}
}
