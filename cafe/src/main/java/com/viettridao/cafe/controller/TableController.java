package com.viettridao.cafe.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.viettridao.cafe.common.TableStatus;
import com.viettridao.cafe.dto.response.tables.TableMenuItemResponse;
import com.viettridao.cafe.dto.response.tables.TableResponse;
import com.viettridao.cafe.model.ReservationEntity;
import com.viettridao.cafe.service.TableService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/sale")
@RequiredArgsConstructor
public class TableController {

	private final TableService tableService;

	@GetMapping
	public String getAllTables(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
			Model model) {
		try {
			if (page < 0) {
				return "redirect:/sale?page=0";
			}

			Page<TableResponse> tablePage = tableService.getAllTables(page, size);

			if (page >= tablePage.getTotalPages() && tablePage.getTotalPages() > 0) {
				return "redirect:/sale?page=" + (tablePage.getTotalPages() - 1);
			}

			model.addAttribute("tables", tablePage.getContent());
			model.addAttribute("currentPage", tablePage.getNumber());
			model.addAttribute("totalPages", tablePage.getTotalPages());
		} catch (Exception e) {
			model.addAttribute("error", "Lỗi khi tải danh sách bàn: " + e.getMessage());
		}

		return "sale/list";
	}

	@GetMapping("/{tableId}/menu")
	public String getTableMenu(@PathVariable Integer tableId, Model model) {
		try {
			List<TableMenuItemResponse> menuItems = tableService.getTableMenuItems(tableId);
			model.addAttribute("menuItems", menuItems);

			TableResponse tableResponse = tableService.getTableById(tableId);
			model.addAttribute("table", tableResponse);

			if (tableResponse.getStatus() != TableStatus.AVAILABLE) {
				ReservationEntity reservation = tableService.getLatestReservationByTableId(tableId);
				model.addAttribute("reservation", reservation);
			}

			String invoiceStatus;
			if (menuItems == null || menuItems.isEmpty()) {
				invoiceStatus = "Chưa có hóa đơn";
			} else {
				var invoice = tableService.getLatestUnpaidInvoiceByTableId(tableId);
				invoiceStatus = (invoice != null) ? invoice.getStatus().name() : "Chưa có hóa đơn";
			}
			model.addAttribute("invoiceStatus", invoiceStatus);
		} catch (Exception e) {
			model.addAttribute("error", "Lỗi khi hiển thị thông tin bàn: " + e.getMessage());
		}

		return "sale/menu";
	}

	@GetMapping("/invoice/create-or-get")
	public String getOrCreateInvoice(@RequestParam("tableId") Integer tableId) {
		try {
			Integer invoiceId = tableService.getOrCreateInvoiceIdByTableId(tableId);
			return "redirect:/invoice/item-form?invoiceId=" + invoiceId;
		} catch (Exception e) {
			return "redirect:/sale?error=" + e.getMessage();
		}
	}
}
