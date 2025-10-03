package com.viettridao.cafe.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.viettridao.cafe.common.InvoiceStatus;
import com.viettridao.cafe.common.TableStatus;
import com.viettridao.cafe.dto.request.invoices.InvoiceItemListRequest;
import com.viettridao.cafe.dto.request.invoices.InvoiceItemRequest;
import com.viettridao.cafe.dto.response.invoices.InvoiceItemResponse;
import com.viettridao.cafe.mapper.InvoiceDetailMapper;
import com.viettridao.cafe.model.InvoiceDetailEntity;
import com.viettridao.cafe.model.InvoiceEntity;
import com.viettridao.cafe.model.InvoiceKey;
import com.viettridao.cafe.model.MenuItemEntity;
import com.viettridao.cafe.model.ReservationEntity;
import com.viettridao.cafe.model.TableEntity;
import com.viettridao.cafe.repository.InvoiceItemDetailRepository;
import com.viettridao.cafe.repository.InvoiceRepository;
import com.viettridao.cafe.repository.MenuItemRepository;
import com.viettridao.cafe.repository.ReservationRepository;
import com.viettridao.cafe.repository.TableRepository;
import com.viettridao.cafe.service.InvoiceItemService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InvoiceItemServiceImpl implements InvoiceItemService {

	private final InvoiceItemDetailRepository invoiceDetailRepository;
	private final InvoiceRepository invoiceRepository;
	private final MenuItemRepository menuItemRepository;
	private final TableRepository tableRepository;
	private final ReservationRepository reservationRepository;
	private final InvoiceDetailMapper invoiceDetailMapper;

	@Override
	@Transactional
	public List<InvoiceItemResponse> addItemsToInvoice(InvoiceItemListRequest request) {
		if (request.getItems() == null || request.getItems().isEmpty()) {
			throw new IllegalArgumentException("Danh sách món không được để trống");
		}

		Integer invoiceId = request.getItems().get(0).getInvoiceId();
		InvoiceEntity invoice = invoiceRepository.findById(invoiceId).orElse(null);

		TableEntity table = tableRepository.findByReservations_Invoice_Id(invoiceId);
		if (table == null) {
			throw new RuntimeException("Không tìm thấy bàn liên kết với hóa đơn ID: " + invoiceId);
		}

		if (!(table.getStatus() == TableStatus.AVAILABLE || table.getStatus() == TableStatus.RESERVED
				|| table.getStatus() == TableStatus.OCCUPIED)) {
			throw new RuntimeException("Bàn không cho phép thêm món");
		}

		if (invoice == null || invoice.getStatus() != InvoiceStatus.UNPAID) {
			invoice = new InvoiceEntity();
			invoice.setStatus(InvoiceStatus.UNPAID);
			invoice.setTotalAmount(0.0);
			invoice.setCreatedAt(LocalDateTime.now());
			invoice.setIsDeleted(false);
			invoice = invoiceRepository.save(invoice);

			ReservationEntity latestReservation = reservationRepository
					.findTopByTable_IdAndIsDeletedFalseOrderByReservationDateDescReservationTimeDesc(table.getId())
					.orElseThrow(() -> new RuntimeException("Không tìm thấy đặt bàn gần nhất để gắn hóa đơn"));

			latestReservation.setInvoice(invoice);
			latestReservation.getId().setIdInvoice(invoice.getId());
			reservationRepository.save(latestReservation);
		}

		double total = 0.0;
		for (InvoiceItemRequest itemReq : request.getItems()) {
			MenuItemEntity menuItem = menuItemRepository.findById(itemReq.getMenuItemId())
					.orElseThrow(() -> new RuntimeException("Không tìm thấy món với ID: " + itemReq.getMenuItemId()));

			InvoiceKey key = new InvoiceKey();
			key.setIdInvoice(invoice.getId());
			key.setIdMenuItem(menuItem.getId());

			InvoiceDetailEntity detail = invoiceDetailRepository.findByIdAndIsDeletedFalse(key).orElse(null);

			if (detail != null) {
				detail.setQuantity(detail.getQuantity() + itemReq.getQuantity());
			} else {
				detail = invoiceDetailMapper.fromRequest(itemReq);
				detail.setInvoice(invoice);
				detail.setMenuItem(menuItem);
				detail.setPrice(menuItem.getCurrentPrice());
			}

			detail.setIsDeleted(false);
			invoiceDetailRepository.save(detail);
		}

		List<InvoiceDetailEntity> allDetails = invoiceDetailRepository
				.findByInvoice_IdAndIsDeletedFalse(invoice.getId());
		for (InvoiceDetailEntity detail : allDetails) {
			total += detail.getQuantity() * detail.getPrice();
		}
		invoice.setTotalAmount(total);
		invoiceRepository.save(invoice);

		table.setStatus(TableStatus.OCCUPIED);
		tableRepository.save(table);

		return invoiceDetailMapper.toDtoList(allDetails);
		
	}
}