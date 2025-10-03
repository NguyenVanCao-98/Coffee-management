package com.viettridao.cafe.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.viettridao.cafe.dto.response.tables.TableMenuItemResponse;
import com.viettridao.cafe.model.InvoiceEntity;
import com.viettridao.cafe.model.ReservationEntity;
import com.viettridao.cafe.service.TableService;

import lombok.RequiredArgsConstructor;

/**
 * Controller dùng để xử lý logic liên quan đến việc in hóa đơn bán hàng cho
 * bàn.
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/sale")
public class PrintController {

	// Khai báo TableService để xử lý các nghiệp vụ liên quan đến bàn và hóa đơn
	private final TableService tableService;

	/**
	 * Hiển thị giao diện in hóa đơn cho bàn có ID tương ứng.
	 * 
	 * @param tableId ID của bàn cần in hóa đơn
	 * @param model   Model để truyền dữ liệu sang view
	 * @return tên view "sale/print"
	 */
	@GetMapping("/{tableId}/print")
	public String printInvoice(@PathVariable Integer tableId, Model model) {
		// Lấy hóa đơn mới nhất (theo thời gian) của bàn được truyền vào
		InvoiceEntity invoice = tableService.getLatestInvoiceByTableId(tableId);

		// Khai báo danh sách món ăn trong hóa đơn
		List<TableMenuItemResponse> items;

		// Khai báo biến lưu tổng tiền hóa đơn
		double total = 0.0;

		// Kiểm tra nếu không có hóa đơn hoặc danh sách chi tiết hóa đơn bị null
		if (invoice == null || invoice.getInvoiceDetails() == null) {
			// Khởi tạo danh sách món ăn rỗng
			items = List.of();
		} else {
			// Nếu có hóa đơn hợp lệ, xử lý danh sách chi tiết hóa đơn
			items = invoice.getInvoiceDetails().stream()
				// Lọc ra các chi tiết hợp lệ: chưa bị xóa, có số lượng > 0 và có đơn giá
				.filter(detail -> (detail.getIsDeleted() == null || !detail.getIsDeleted())
					&& detail.getQuantity() != null && detail.getQuantity() > 0 && detail.getPrice() != null)
				// Chuyển đổi từ entity sang DTO để dùng trong view
				.map(detail -> {
					// Tạo đối tượng DTO chứa thông tin món ăn
					TableMenuItemResponse dto = new TableMenuItemResponse();
					dto.setMenuItemId(detail.getMenuItem().getId()); // ID món
					dto.setItemName(detail.getMenuItem().getItemName()); // Tên món
					dto.setQuantity(detail.getQuantity()); // Số lượng
					dto.setPrice(detail.getPrice()); // Đơn giá
					dto.setAmount(detail.getPrice() * detail.getQuantity()); // Thành tiền = đơn giá * số lượng
					return dto; // Trả về DTO
				})
				// Chuyển stream về list
				.toList();

			// Tính tổng tiền từ danh sách món ăn đã xử lý
			total = items.stream().mapToDouble(TableMenuItemResponse::getAmount).sum();
		}

		// Lấy thông tin đặt bàn gần nhất để truy xuất tên nhân viên phục vụ
		ReservationEntity reservation = tableService.getLatestReservationByTableId(tableId);

		// Lấy tên nhân viên nếu có, nếu không thì gán là "Không xác định"
		String employeeName = (reservation != null && reservation.getEmployee() != null)
			? reservation.getEmployee().getFullName()
			: "Không xác định";

		// Truyền danh sách món ăn sang view
		model.addAttribute("items", items);

		// Truyền tổng tiền sang view
		model.addAttribute("totalAmount", total);

		// Truyền tên nhân viên phục vụ sang view
		model.addAttribute("employeeName", employeeName);

		// Truyền ID bàn sang view
		model.addAttribute("tableId", tableId);

		// Trả về view in hóa đơn
		return "sale/print";
	}
}