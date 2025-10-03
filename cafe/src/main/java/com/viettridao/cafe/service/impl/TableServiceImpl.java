package com.viettridao.cafe.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.viettridao.cafe.common.InvoiceStatus;
import com.viettridao.cafe.common.TableStatus;
import com.viettridao.cafe.dto.response.tables.TableMenuItemResponse;
import com.viettridao.cafe.dto.response.tables.TableResponse;
import com.viettridao.cafe.mapper.TableMapper;
import com.viettridao.cafe.model.EmployeeEntity;
import com.viettridao.cafe.model.InvoiceEntity;
import com.viettridao.cafe.model.ReservationEntity;
import com.viettridao.cafe.model.ReservationKey;
import com.viettridao.cafe.model.TableEntity;
import com.viettridao.cafe.repository.EmployeeRepository;
import com.viettridao.cafe.repository.InvoiceRepository;
import com.viettridao.cafe.repository.ReservationRepository;
import com.viettridao.cafe.repository.TableRepository;
import com.viettridao.cafe.service.TableService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TableServiceImpl implements TableService {

	private final TableRepository tableRepository;
	private final InvoiceRepository invoiceRepository;
	private final ReservationRepository reservationRepository;
	private final EmployeeRepository employeeRepository;
	private final TableMapper tableMapper;

	@Override
	public Page<TableResponse> getAllTables(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<TableEntity> entityPage = tableRepository.findByIsDeletedFalse(pageable);

		List<TableResponse> responses = entityPage.getContent().stream().map(table -> {
			TableResponse response = tableMapper.toDto(table);

			if (table.getStatus() == TableStatus.AVAILABLE || table.getStatus() == TableStatus.RESERVED
					|| table.getStatus() == TableStatus.OCCUPIED) {

				InvoiceEntity invoice = getLatestUnpaidInvoiceByTableId(table.getId());
				if (invoice != null) {
					response.setInvoiceId(invoice.getId());
				}
			}
			return response;
		}).toList();

		return new PageImpl<>(responses, pageable, entityPage.getTotalElements());
	}

	@Override
	public List<TableMenuItemResponse> getTableMenuItems(Integer tableId) {
		InvoiceEntity invoice = getLatestUnpaidInvoiceByTableId(tableId);
		if (invoice == null || invoice.getInvoiceDetails() == null)
			return List.of();

		return invoice.getInvoiceDetails().stream()
				.filter(detail -> (detail.getIsDeleted() == null || !detail.getIsDeleted())
						&& detail.getQuantity() != null && detail.getQuantity() > 0 && detail.getPrice() != null)
				.map(detail -> {
					TableMenuItemResponse dto = new TableMenuItemResponse();
					dto.setMenuItemId(detail.getMenuItem().getId());
					dto.setItemName(detail.getMenuItem().getItemName());
					dto.setQuantity(detail.getQuantity());
					dto.setPrice(detail.getPrice());
					dto.setAmount(detail.getPrice() * detail.getQuantity());
					return dto;
				}).toList();
	}

	@Override
	public InvoiceEntity getLatestUnpaidInvoiceByTableId(Integer tableId) {
		return invoiceRepository.findTopByReservations_Table_IdAndStatusAndIsDeletedFalseOrderByCreatedAtDesc(tableId,
				InvoiceStatus.UNPAID);
	}

	@Override
	public ReservationEntity getLatestReservationByTableId(Integer tableId) {
		return reservationRepository
				.findTopByTable_IdAndIsDeletedFalseOrderByReservationDateDescReservationTimeDesc(tableId).orElse(null);
	}

	@Override
	public InvoiceEntity getLatestInvoiceByTableId(Integer tableId) {
		return invoiceRepository.findTopByReservations_Table_IdAndIsDeletedFalseOrderByCreatedAtDesc(tableId)
				.orElse(null);
	}

	@Override
	public TableResponse getTableById(Integer tableId) {
		TableEntity table = tableRepository.findById(tableId)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy bàn ID: " + tableId));
		return tableMapper.toDto(table);
	}

	@Transactional
	@Override
	public Integer getOrCreateInvoiceIdByTableId(Integer tableId) {
		TableEntity table = tableRepository.findById(tableId)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy bàn ID: " + tableId));

		if (!(table.getStatus() == TableStatus.AVAILABLE || table.getStatus() == TableStatus.RESERVED
				|| table.getStatus() == TableStatus.OCCUPIED)) {
			throw new RuntimeException("Bàn không ở trạng thái hợp lệ để tạo hóa đơn.");
		}

		InvoiceEntity invoice = getLatestUnpaidInvoiceByTableId(tableId);
		if (invoice != null)
			return invoice.getId();

		invoice = createNewInvoiceAndReservation(table);
		return invoice.getId();
	}

	private InvoiceEntity createNewInvoiceAndReservation(TableEntity table) {
		InvoiceEntity invoice = new InvoiceEntity();
		invoice.setTotalAmount(0.0);
		invoice.setStatus(InvoiceStatus.UNPAID);
		invoice.setCreatedAt(LocalDateTime.now());
		invoice.setIsDeleted(false);
		invoice = invoiceRepository.save(invoice);

		EmployeeEntity defaultEmployee = employeeRepository.findById(1)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên mặc định"));

		ReservationEntity reservation = new ReservationEntity();
		ReservationKey key = new ReservationKey();
		key.setIdInvoice(invoice.getId());
		key.setIdTable(table.getId());
		key.setIdEmployee(defaultEmployee.getId());

		reservation.setId(key);
		reservation.setInvoice(invoice);
		reservation.setTable(table);
		reservation.setEmployee(defaultEmployee);
		reservation.setCustomerName("Khách vãng lai");
		reservation.setCustomerPhone("N/A");
		reservation.setReservationDate(LocalDate.now());
		reservation.setReservationTime(LocalDateTime.now().toLocalTime());
		reservation.setIsDeleted(false);
		reservationRepository.save(reservation);


		return invoice;
	}
}
