package com.viettridao.cafe.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.viettridao.cafe.service.TableClearService;

import lombok.RequiredArgsConstructor;

/**
 * Controller xử lý thao tác liên quan đến việc hủy (dọn) bàn sau khi thanh toán.
 *
 * Khi người dùng nhấn "Hủy bàn" hoặc "Dọn bàn", controller này sẽ gọi service
 * để thực hiện việc cập nhật trạng thái bàn và dữ liệu liên quan.
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/sale")
public class SalesController {

	// Khai báo service dùng để xử lý chức năng dọn bàn (clear bàn)
	private final TableClearService tableClearService;

	/**
	 * Xử lý yêu cầu hủy bàn (dọn bàn) dựa trên ID bàn được gửi từ phía client.
	 * Nếu thành công, hiển thị thông báo thành công. Nếu có lỗi, hiển thị lỗi.
	 *
	 * @param tableId ID của bàn cần hủy
	 * @param redirectAttributes đối tượng dùng để truyền thông báo giữa các request
	 * @return điều hướng về trang /sale
	 */
	@PostMapping("/clear") // Định nghĩa endpoint xử lý yêu cầu POST đến "/clear"
	public String clearTable(@RequestParam("tableId") Integer tableId, RedirectAttributes redirectAttributes) {
		try {
			// Gọi service để dọn bàn với ID tương ứng
			tableClearService.clearTable(tableId);

			// Nếu thành công, thêm thông báo thành công vào redirectAttributes để hiển thị sau khi chuyển trang
			redirectAttributes.addFlashAttribute("success", "Đã hủy bàn thành công.");
		} catch (Exception e) {
			// Nếu xảy ra lỗi, thêm thông báo lỗi vào redirectAttributes
			redirectAttributes.addFlashAttribute("error", "Lỗi khi hủy bàn: " + e.getMessage());
		}

		// Chuyển hướng về trang /sale sau khi xử lý xong
		return "redirect:/sale";
	}
}