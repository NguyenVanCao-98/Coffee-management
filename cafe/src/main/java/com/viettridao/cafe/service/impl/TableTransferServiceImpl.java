package com.viettridao.cafe.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.viettridao.cafe.common.InvoiceStatus;
import com.viettridao.cafe.common.TableStatus;
import com.viettridao.cafe.model.InvoiceEntity;
import com.viettridao.cafe.model.ReservationEntity;
import com.viettridao.cafe.model.ReservationKey;
import com.viettridao.cafe.model.TableEntity;
import com.viettridao.cafe.repository.InvoiceRepository;
import com.viettridao.cafe.repository.ReservationRepository;
import com.viettridao.cafe.repository.TableRepository;
import com.viettridao.cafe.service.TableTransferService;

import lombok.RequiredArgsConstructor;

/**
 * Triển khai dịch vụ chuyển bàn trong quán cà phê. Chức năng chính là chuyển
 * hóa đơn chưa thanh toán và đặt chỗ từ bàn nguồn sang bàn đích.
 */
@Service
@RequiredArgsConstructor
public class TableTransferServiceImpl implements TableTransferService {

	// Repository dùng để thao tác với bảng Table trong DB
	private final TableRepository tableRepository;

	// Repository dùng để thao tác với bảng Invoice trong DB
	private final InvoiceRepository invoiceRepository;

	// Repository dùng để thao tác với bảng Reservation trong DB
	private final ReservationRepository reservationRepository;

	/**
	 * Chuyển hóa đơn chưa thanh toán và đặt chỗ từ bàn nguồn sang bàn đích.
	 * 
	 * @param fromTableId ID bàn nguồn (bàn hiện đang có hóa đơn chưa thanh toán)
	 * @param toTableId   ID bàn đích (bàn mà hóa đơn sẽ được chuyển sang)
	 */
	@Override
	@Transactional
	public void transferTable(Integer fromTableId, Integer toTableId) {
		// Kiểm tra không được phép chuyển bàn sang chính nó
		if (fromTableId.equals(toTableId)) {
			throw new IllegalArgumentException("Không thể chuyển sang cùng một bàn.");
		}

		// Lấy thông tin bàn nguồn từ DB, nếu không có báo lỗi
		TableEntity fromTable = tableRepository.findById(fromTableId)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy bàn nguồn."));

		// Lấy thông tin bàn đích từ DB, nếu không có báo lỗi
		TableEntity toTable = tableRepository.findById(toTableId)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy bàn đích."));

		// Lấy trạng thái của bàn nguồn và bàn đích để kiểm tra hợp lệ
		TableStatus fromStatus = fromTable.getStatus();
		TableStatus toStatus = toTable.getStatus();

		// Kiểm tra điều kiện hợp lệ cho việc chuyển bàn
		// - Không chuyển từ bàn trống sang bàn đã đặt trước hoặc đang phục vụ
		// - Không chuyển từ bàn đã đặt hoặc đang phục vụ sang bàn không trống
		// - Không chuyển giữa 2 bàn đều đã đặt trước hoặc đều đang phục vụ
		if ((fromStatus == TableStatus.AVAILABLE
				&& (toStatus == TableStatus.RESERVED || toStatus == TableStatus.OCCUPIED))
				|| (fromStatus == TableStatus.RESERVED && toStatus != TableStatus.AVAILABLE)
				|| (fromStatus == TableStatus.OCCUPIED && toStatus != TableStatus.AVAILABLE)
				|| (fromStatus == TableStatus.RESERVED && toStatus == TableStatus.RESERVED)
				|| (fromStatus == TableStatus.OCCUPIED && toStatus == TableStatus.OCCUPIED)) {
			throw new RuntimeException("Chuyển bàn không hợp lệ.");
		}

		// Lấy hóa đơn chưa thanh toán gần nhất của bàn nguồn
		InvoiceEntity invoice = invoiceRepository
				.findTopByReservations_Table_IdAndStatusAndIsDeletedFalseOrderByCreatedAtDesc(fromTableId,
						InvoiceStatus.UNPAID);

		// Nếu bàn nguồn không có hóa đơn chưa thanh toán thì không thể chuyển
		if (invoice == null) {
			throw new RuntimeException("Không có hóa đơn chưa thanh toán ở bàn nguồn.");
		}

		// Kiểm tra bàn đích có hóa đơn chưa thanh toán chưa
		InvoiceEntity targetInvoice = invoiceRepository
				.findTopByReservations_Table_IdAndStatusAndIsDeletedFalseOrderByCreatedAtDesc(toTableId,
						InvoiceStatus.UNPAID);

		// Nếu bàn đích đã có hóa đơn chưa thanh toán, không thể chuyển sang
		if (targetInvoice != null) {
			throw new RuntimeException("Bàn đích đang có hóa đơn chưa thanh toán.");
		}

		// Lấy danh sách các đặt chỗ chưa bị xóa thuộc hóa đơn bàn nguồn
		List<ReservationEntity> oldReservations = reservationRepository
				.findByInvoice_IdAndIsDeletedFalse(invoice.getId());

		// Xóa đặt chỗ cũ của bàn nguồn, tạo đặt chỗ mới cho bàn đích với các thông tin
		// giữ nguyên
		for (ReservationEntity old : oldReservations) {
			// Xóa đặt chỗ cũ để tránh trùng lặp
			reservationRepository.delete(old);

			// Tạo đặt chỗ mới cho bàn đích
			ReservationEntity newReservation = new ReservationEntity();
			ReservationKey newKey = new ReservationKey();

			// Gán khóa mới gồm ID hóa đơn, ID bàn đích, ID nhân viên giữ nguyên
			newKey.setIdInvoice(old.getInvoice().getId());
			newKey.setIdTable(toTableId);
			newKey.setIdEmployee(old.getEmployee().getId());

			// Thiết lập các thông tin đặt chỗ mới
			newReservation.setId(newKey);
			newReservation.setInvoice(old.getInvoice());
			newReservation.setTable(toTable);
			newReservation.setEmployee(old.getEmployee());
			newReservation.setCustomerName(old.getCustomerName());
			newReservation.setCustomerPhone(old.getCustomerPhone());
			newReservation.setReservationDate(old.getReservationDate());
			newReservation.setReservationTime(old.getReservationTime());
			newReservation.setIsDeleted(false);

			// Lưu đặt chỗ mới vào DB
			reservationRepository.save(newReservation);
		}

		// Cập nhật trạng thái bàn theo trạng thái bàn nguồn và bàn đích
		if (fromStatus == TableStatus.RESERVED && toStatus == TableStatus.AVAILABLE) {
			// Chuyển từ bàn đã đặt sang bàn trống
			fromTable.setStatus(TableStatus.AVAILABLE);
			toTable.setStatus(TableStatus.RESERVED);
		} else if (fromStatus == TableStatus.OCCUPIED && toStatus == TableStatus.AVAILABLE) {
			// Chuyển từ bàn đang phục vụ sang bàn trống
			fromTable.setStatus(TableStatus.AVAILABLE);
			toTable.setStatus(TableStatus.OCCUPIED);
		}

		// Lưu trạng thái bàn nguồn và bàn đích sau khi chuyển
		tableRepository.save(fromTable);
		tableRepository.save(toTable);
	}
}
