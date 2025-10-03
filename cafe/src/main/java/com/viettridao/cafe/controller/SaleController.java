package com.viettridao.cafe.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.viettridao.cafe.common.TableStatus;
import com.viettridao.cafe.dto.request.tables.MenuItemSplitRequest;
import com.viettridao.cafe.dto.request.tables.MenuItemSplitWrapper;
import com.viettridao.cafe.dto.response.tables.TableMenuItemResponse;
import com.viettridao.cafe.model.InvoiceEntity;
import com.viettridao.cafe.model.TableEntity;
import com.viettridao.cafe.repository.TableRepository;
import com.viettridao.cafe.service.TableService;
import com.viettridao.cafe.service.TableSplitService;

import lombok.RequiredArgsConstructor;

/**
 * Controller xử lý các chức năng liên quan đến giao dịch bán hàng, bao gồm hiển
 * thị form tách bàn và xử lý logic tách món từ bàn nguồn sang bàn đích.
 */

@Controller
@RequiredArgsConstructor
@RequestMapping("/sale")
public class SaleController {

	// Khai báo TableRepository để truy xuất thông tin bàn từ cơ sở dữ liệu
	private final TableRepository tableRepository;

	// Khai báo TableService để xử lý các nghiệp vụ liên quan đến bàn
	private final TableService tableService;

	// Khai báo TableSplitService để thực hiện logic tách bàn
	private final TableSplitService tableSplitService;

	/**
	 * Hiển thị form tách bàn, với thông tin món ăn, danh sách bàn trống,...
	 */
	@GetMapping("/split")
	public String showSplitForm(@RequestParam("fromTableId") Integer fromTableId, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			// Lấy thông tin bàn nguồn theo ID
			TableEntity fromTable = tableRepository.findById(fromTableId)
					.orElseThrow(() -> new RuntimeException("Không tìm thấy bàn nguồn"));

			if (fromTable.getStatus() == TableStatus.RESERVED) {
				redirectAttributes.addFlashAttribute("error", "Bàn này đã được đặt trước, không thể tách.");
				return "redirect:/sale";
			}

			InvoiceEntity invoice = tableService.getLatestUnpaidInvoiceByTableId(fromTableId);
			if (invoice == null) {
				redirectAttributes.addFlashAttribute("error", "Bàn này đang trống, không thể tách.");
				return "redirect:/sale";
			}

			// Lấy danh sách món ăn của bàn
			List<TableMenuItemResponse> items = tableService.getTableMenuItems(fromTableId);

			// Lấy danh sách các bàn chưa bị xóa
			List<TableEntity> allTables = tableRepository.findByIsDeletedFalse();

			// --- Khởi tạo wrapper để binding form ---
			MenuItemSplitWrapper wrapper = new MenuItemSplitWrapper();
			List<MenuItemSplitRequest> splitItems = items.stream().map(i -> {
				MenuItemSplitRequest req = new MenuItemSplitRequest();
				req.setMenuItemId(i.getMenuItemId());
				req.setSelected(false); // quan trọng: mặc định chưa chọn
				req.setQuantityToMove(null); // mặc định null
				return req;
			}).toList();
			wrapper.setItems(new ArrayList<>(splitItems));

			// Đưa dữ liệu vào model
			model.addAttribute("fromTableId", fromTableId);
			model.addAttribute("menuItems", items);
			model.addAttribute("allTables", allTables.stream().filter(t -> !t.getId().equals(fromTableId)).toList());
			model.addAttribute("splitWrapper", wrapper);

			return "sale/split-form";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Lỗi khi hiển thị form tách bàn: " + e.getMessage());
			return "redirect:/sale";
		}
	}

	/**
	 * Xử lý logic tách bàn: chuyển món từ bàn nguồn sang bàn đích mới.
	 */
	@PostMapping("/split")
	public String splitTable(@RequestParam("fromTableId") Integer fromTableId,
			@RequestParam("toTableId") Integer toTableId, @RequestParam("customerName") String customerName,
			@RequestParam("customerPhone") String customerPhone,
			@ModelAttribute("splitWrapper") MenuItemSplitWrapper wrapper, BindingResult bindingResult,
			RedirectAttributes redirectAttributes, Model model) {

		// Lấy dữ liệu bàn và món
		TableEntity fromTable = tableRepository.findById(fromTableId)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy bàn nguồn"));

		List<TableMenuItemResponse> items = tableService.getTableMenuItems(fromTableId);
		List<TableEntity> allTables = tableRepository.findByIsDeletedFalse();

		// Luôn add các dữ liệu cần thiết vào model để hiển thị form
		model.addAttribute("fromTableId", fromTableId);
		model.addAttribute("menuItems", items);
		model.addAttribute("allTables", allTables.stream().filter(t -> !t.getId().equals(fromTableId))
				.filter(t -> t.getStatus() == TableStatus.AVAILABLE || t.getStatus() == TableStatus.OCCUPIED).toList());
		model.addAttribute("splitWrapper", wrapper);
		model.addAttribute("customerName", customerName);
		model.addAttribute("customerPhone", customerPhone);
		model.addAttribute("toTableId", toTableId);

		// Validate tên khách
		if (customerName == null || customerName.trim().isEmpty() || customerName.length() > 30) {
			model.addAttribute("customerNameError", "Tên khách hàng không hợp lệ.");
			return "sale/split-form";
		}

		// Validate số điện thoại
		if (!customerPhone.matches("\\d{10,11}")) {
			model.addAttribute("customerPhoneError", "Số điện thoại không hợp lệ. Chỉ gồm 10 hoặc 11 chữ số.");
			return "sale/split-form";
		}

		try {
			TableEntity toTable = tableRepository.findById(toTableId)
					.orElseThrow(() -> new RuntimeException("Không tìm thấy bàn đích"));

			if (toTable.getStatus() != TableStatus.AVAILABLE && toTable.getStatus() != TableStatus.OCCUPIED) {
				model.addAttribute("error",
						"Chỉ có thể tách sang bàn trống hoặc bàn đang phục vụ (AVAILABLE/OCCUPIED).");
				return "sale/split-form";
			}

			// --- Xử lý chọn món ---
			List<MenuItemSplitRequest> itemsList = wrapper.getItems();

			// 1. Kiểm tra có ít nhất 1 món được tích
			List<MenuItemSplitRequest> selectedItems = itemsList.stream()
					.filter(i -> Boolean.TRUE.equals(i.getSelected())).toList();

			if (selectedItems.isEmpty()) {
				model.addAttribute("error", "Vui lòng chọn ít nhất 1 món để tách.");
				return "sale/split-form";
			}

			// 2. Kiểm tra số lượng chỉ cho những món đã tích
			boolean hasInvalidQty = selectedItems.stream()
					.anyMatch(i -> i.getQuantityToMove() == null || i.getQuantityToMove() <= 0);

			if (hasInvalidQty) {
				model.addAttribute("error", "Số lượng tách không được để trống hoặc ≤0.");
				return "sale/split-form";
			}

			// 3. Kiểm tra số lượng tách không vượt quá số lượng hiện có
			List<TableMenuItemResponse> currentItems = tableService.getTableMenuItems(fromTableId);
			for (MenuItemSplitRequest item : selectedItems) {
				int availableQty = currentItems.stream()
						.filter(menuItem -> menuItem.getMenuItemId().equals(item.getMenuItemId())).findFirst()
						.map(TableMenuItemResponse::getQuantity).orElse(0);
				if (item.getQuantityToMove() > availableQty) {
					model.addAttribute("error",
							"Số lượng muốn tách vượt quá số lượng hiện có cho món: " + item.getMenuItemId());
					return "sale/split-form";
				}
			}

			// Thực hiện tách bàn
			tableSplitService.splitTable(fromTableId, toTableId, selectedItems, customerName, customerPhone);

			redirectAttributes.addFlashAttribute("success", "Tách bàn thành công!");
			return "redirect:/sale";

		} catch (Exception e) {
			model.addAttribute("error", "Lỗi khi tách bàn: " + e.getMessage());
			return "sale/split-form";
		}
	}
}