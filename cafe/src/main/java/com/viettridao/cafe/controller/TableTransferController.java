package com.viettridao.cafe.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.viettridao.cafe.common.TableStatus;
import com.viettridao.cafe.model.TableEntity;
import com.viettridao.cafe.repository.TableRepository;
import com.viettridao.cafe.service.TableTransferService;

import lombok.RequiredArgsConstructor;

/**
 * Controller xử lý các yêu cầu liên quan đến chuyển bàn trong khu vực bán hàng.
 * 
 * Chức năng chính: - Hiển thị giao diện chuyển bàn - Thực hiện chuyển bàn từ
 * bàn nguồn sang bàn đích
 * 
 * Đường dẫn gốc: /sale
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/sale")
public class TableTransferController {

	// Khai báo repository dùng để truy vấn dữ liệu liên quan đến bàn
	private final TableRepository tableRepository;

	// Khai báo service dùng để xử lý logic chuyển bàn
	private final TableTransferService tableTransferService;

	/**
	 * Hiển thị giao diện chuyển bàn.
	 * 
	 * @param fromTableId        ID của bàn nguồn cần chuyển
	 * @param model              Đối tượng Model để truyền dữ liệu ra view
	 * @param redirectAttributes Đối tượng để truyền thông báo khi redirect
	 * @return trang hiển thị form chuyển bàn hoặc redirect về trang sale nếu lỗi
	 */
	@GetMapping("/transfer-table")
	public String showTransferTableForm(
	        @RequestParam("from") Integer fromTableId,
	        Model model,
	        RedirectAttributes redirectAttributes
	) {
	    try {
	        TableEntity fromTable = tableRepository.findById(fromTableId)
	                .orElseThrow(() -> new RuntimeException("Không tìm thấy bàn nguồn."));

	        // Lấy danh sách bàn trống (enum TableStatus.AVAILABLE)
	        List<TableEntity> availableTables = tableRepository.findByIsDeletedFalseAndStatus(TableStatus.AVAILABLE);

	        model.addAttribute("fromTableId", fromTableId);
	        model.addAttribute("fromTableName", fromTable.getTableName());
	        model.addAttribute("availableTables", availableTables);

	        return "sale/transfer-table";
	    } catch (Exception e) {
	        redirectAttributes.addFlashAttribute("error", "Lỗi khi hiển thị chuyển bàn: " + e.getMessage());
	        return "redirect:/sale";
	    }
	}



	/**
	 * Thực hiện chuyển thông tin từ bàn nguồn sang bàn đích.
	 * 
	 * @param fromTableId        ID bàn nguồn
	 * @param toTableId          ID bàn đích
	 * @param redirectAttributes Đối tượng để truyền thông báo khi redirect
	 * @return redirect về trang /sale kèm thông báo thành công/thất bại
	 */
	@PostMapping("/transfer-table") // Mapping yêu cầu POST tới đường dẫn /transfer-table
	public String transferTable(
		// Nhận ID bàn nguồn từ form submit
		@RequestParam("fromTableId") Integer fromTableId,
		// Nhận ID bàn đích từ form submit
		@RequestParam("toTableId") Integer toTableId,
		// Dùng để truyền thông báo redirect sau khi xử lý
		RedirectAttributes redirectAttributes
	) {
		try {
			// Gọi service thực hiện logic chuyển bàn
			tableTransferService.transferTable(fromTableId, toTableId);

			// Thêm thông báo thành công vào redirect
			redirectAttributes.addFlashAttribute("success", "Chuyển bàn thành công.");
		} catch (Exception e) {
			// Nếu có lỗi, thêm thông báo lỗi vào redirect
			redirectAttributes.addFlashAttribute("error", "Lỗi khi chuyển bàn: " + e.getMessage());
		}

		// Sau khi xử lý, quay về trang /sale
		return "redirect:/sale";
	}
}