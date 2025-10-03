package com.viettridao.cafe.service.impl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.viettridao.cafe.common.InvoiceStatus;
import com.viettridao.cafe.common.TableStatus;
import com.viettridao.cafe.dto.request.tables.TableBookingRequest;
import com.viettridao.cafe.dto.response.tables.TableBookingResponse;
import com.viettridao.cafe.mapper.ReservationMapper;
import com.viettridao.cafe.model.EmployeeEntity;
import com.viettridao.cafe.model.InvoiceEntity;
import com.viettridao.cafe.model.ReservationEntity;
import com.viettridao.cafe.model.ReservationKey;
import com.viettridao.cafe.model.TableEntity;
import com.viettridao.cafe.repository.EmployeeRepository;
import com.viettridao.cafe.repository.InvoiceRepository;
import com.viettridao.cafe.repository.ReservationRepository;
import com.viettridao.cafe.repository.TableRepository;
import com.viettridao.cafe.service.ReservationService;

import lombok.RequiredArgsConstructor;

/**
 * Service xử lý logic liên quan đến đặt bàn (Reservation). Bao gồm kiểm tra
 * trạng thái bàn, tạo hóa đơn, và lưu thông tin đặt bàn.
 */
@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

	// Khai báo các repository và mapper cần sử dụng
	private final ReservationRepository reservationRepository;
	private final TableRepository tableRepository;
	private final EmployeeRepository employeeRepository;
	private final InvoiceRepository invoiceRepository;
	private final ReservationMapper reservationMapper;

	/**
	 * Đặt bàn dựa trên thông tin yêu cầu từ khách hàng. Kiểm tra bàn có đang bị đặt
	 * hoặc sử dụng không, tạo hóa đơn mới, và lưu đặt bàn.
	 * 
	 * @param request yêu cầu đặt bàn (chứa id bàn, ngày giờ, thông tin khách và
	 *                nhân viên)
	 * @return phản hồi đặt bàn thành công hay thất bại cùng thông báo
	 */
	@Override
	public TableBookingResponse bookTable(TableBookingRequest request) {
		// Lấy bàn theo ID, nếu không tìm thấy thì báo lỗi
		TableEntity table = tableRepository.findById(request.getTableId())
				.orElseThrow(() -> new RuntimeException("Không tìm thấy bàn"));

		// Kiểm tra trạng thái bàn có thể đặt được hay không
		if (table.getStatus() == TableStatus.RESERVED) {
			return new TableBookingResponse(false, "❌ Bàn đã được đặt trước.");
		} else if (table.getStatus() == TableStatus.OCCUPIED) {
			return new TableBookingResponse(false, "❌ Bàn đang được sử dụng.");
		}

		// Kiểm tra bàn đã có người đặt trong cùng ngày, giờ chưa
		boolean exists = reservationRepository.existsByTableIdAndReservationDateAndReservationTimeAndIsDeletedFalse(
				request.getTableId(), request.getReservationDate(), request.getReservationTime());
		if (exists) {
			return new TableBookingResponse(false, "❌ Bàn đã có người đặt tại thời điểm này.");
		}

		// Lấy thông tin nhân viên phục vụ, nếu không tìm thấy thì báo lỗi
		EmployeeEntity employee = employeeRepository.findById(request.getEmployeeId())
				.orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên với ID: " + request.getEmployeeId()));

		// Tạo hóa đơn mới với trạng thái UNPAID, tổng tiền 0
		InvoiceEntity invoice = new InvoiceEntity();
		invoice.setTotalAmount(0.0);
		invoice.setStatus(InvoiceStatus.UNPAID);
		invoice.setCreatedAt(LocalDateTime.now());
		invoice.setIsDeleted(false);
		invoice = invoiceRepository.save(invoice);

		// Tạo entity đặt bàn mới và thiết lập thông tin
		ReservationEntity reservation = new ReservationEntity();
		reservation.setCustomerName(request.getCustomerName());
		reservation.setCustomerPhone(request.getCustomerPhone());
		reservation.setReservationDate(request.getReservationDate());
		reservation.setReservationTime(request.getReservationTime());
		reservation.setIsDeleted(false);

		// Gán quan hệ với bàn, nhân viên và hóa đơn vừa tạo
		reservation.setTable(table);
		reservation.setEmployee(employee);
		reservation.setInvoice(invoice);

		// Tạo khóa chính phức hợp cho đặt bàn dựa trên ID bàn, nhân viên và hóa đơn
		ReservationKey key = new ReservationKey();
		key.setIdTable(table.getId());
		key.setIdEmployee(employee.getId());
		key.setIdInvoice(invoice.getId());
		reservation.setId(key);

		// Lưu đặt bàn vào cơ sở dữ liệu
		reservationRepository.save(reservation);

		// Cập nhật trạng thái bàn thành RESERVED
		table.setStatus(TableStatus.RESERVED);
		tableRepository.save(table);

		// Trả về kết quả đặt bàn thành công kèm mã hóa đơn và tên khách hàng
		return new TableBookingResponse(true,
				"✅ Đặt bàn thành công! Mã hóa đơn #" + invoice.getId() + " cho khách " + request.getCustomerName());
	}
}
