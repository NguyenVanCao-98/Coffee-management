package com.viettridao.cafe.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.viettridao.cafe.common.TableStatus;
import com.viettridao.cafe.model.InvoiceDetailEntity;
import com.viettridao.cafe.model.InvoiceEntity;
import com.viettridao.cafe.model.TableEntity;
import com.viettridao.cafe.repository.InvoiceDetailRepository;
import com.viettridao.cafe.repository.InvoiceRepository;
import com.viettridao.cafe.repository.TableRepository;
import com.viettridao.cafe.service.TableClearService;

import lombok.RequiredArgsConstructor;

/**
 * Service xử lý nghiệp vụ dọn bàn (clear table), bao gồm xóa mềm hóa đơn chưa
 * thanh toán, chi tiết hóa đơn, đặt bàn liên quan và cập nhật trạng thái bàn.
 */
@Service
@RequiredArgsConstructor
public class TableClearServiceImpl implements TableClearService {

	// Khai báo các repository để thao tác với CSDL
	private final TableRepository tableRepository;
	private final InvoiceRepository invoiceRepository;
	private final InvoiceDetailRepository invoiceDetailRepository;

	/**
	 * Dọn bàn theo ID bàn truyền vào. Kiểm tra trạng thái bàn, xóa mềm các hóa đơn,
	 * chi tiết, đặt bàn liên quan. Cập nhật trạng thái bàn về AVAILABLE.
	 * 
	 * @param tableId ID bàn cần dọn
	 * @throws RuntimeException nếu bàn không tồn tại hoặc đang ở trạng thái
	 *                          OCCUPIED
	 */
	@Transactional
	@Override
	public void clearTable(Integer tableId) {
		// Lấy bàn theo ID, nếu không tìm thấy thì báo lỗi
		TableEntity table = tableRepository.findById(tableId)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy bàn ID: " + tableId));

		// Không cho phép dọn bàn nếu đang OCCUPIED
		if (table.getStatus() == TableStatus.OCCUPIED) {
			throw new RuntimeException("Không thể hủy bàn đang phục vụ (OCCUPIED).");
		}

		// Lấy hóa đơn chưa thanh toán gần nhất của bàn
		InvoiceEntity invoice = invoiceRepository
				.findTopByReservations_Table_IdAndStatusAndIsDeletedFalseOrderByCreatedAtDesc(tableId,
						com.viettridao.cafe.common.InvoiceStatus.UNPAID);

		if (invoice != null) {
			// Xóa mềm chi tiết hóa đơn liên quan
			List<InvoiceDetailEntity> details = invoice.getInvoiceDetails();
			if (details != null && !details.isEmpty()) {
				details.forEach(detail -> detail.setIsDeleted(true));
				invoiceDetailRepository.saveAll(details);
			}

			// Xóa mềm các đặt bàn liên quan đến hóa đơn
			if (invoice.getReservations() != null) {
				invoice.getReservations().forEach(reservation -> {
					reservation.setIsDeleted(true);
				});
			}

			// Reset tổng tiền và đánh dấu xóa mềm hóa đơn
			invoice.setTotalAmount(0.0);
			invoice.setIsDeleted(true);
			invoiceRepository.save(invoice);
		}

		// Cập nhật trạng thái bàn về AVAILABLE (sẵn sàng phục vụ)
		table.setStatus(TableStatus.AVAILABLE);
		tableRepository.save(table);
	}
}
