package com.viettridao.cafe.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;

/**
 * Controller xử lý các chức năng sao lưu và phục hồi dữ liệu hệ thống.
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/data")
public class DataController {

	/**
	 * Hiển thị trang giao diện phục hồi dữ liệu.
	 */
	@GetMapping("/restore")
	public String restore(Model model) {
		return "/datas/restore"; // Trả về giao diện restore.html
	}

	/**
	 * Hiển thị trang giao diện sao lưu dữ liệu.
	 */
	@GetMapping("/backup")
	public String backup(Model model) {
		return "/datas/backup"; // Trả về giao diện backup.html
	}

	/**
	 * Xử lý yêu cầu sao lưu dữ liệu từ form.
	 *
	 * @param path               Đường dẫn người dùng muốn lưu file backup
	 * @param redirectAttributes Đối tượng để gửi thông báo flash khi redirect
	 */
	@PostMapping("/backup")
	public String handleBackup(@RequestParam("path") String path, RedirectAttributes redirectAttributes) {
		try {
			// TODO: Gọi logic sao lưu tại đây (ví dụ: ghi file SQL vào path)
			System.out.println("Backup to path: " + path);

			// Gửi thông báo thành công
			redirectAttributes.addFlashAttribute("success", "Đã sao lưu dữ liệu!");
		} catch (Exception e) {
			// Gửi thông báo lỗi nếu xảy ra exception
			redirectAttributes.addFlashAttribute("error", "Đã có lỗi xảy ra khi sao lưu!");
		}

		return "redirect:/data/backup";
	}

	/**
	 * Xử lý yêu cầu phục hồi dữ liệu từ file upload.
	 *
	 * @param file               Tệp backup được người dùng upload lên
	 * @param redirectAttributes Đối tượng để gửi thông báo flash khi redirect
	 */
	@PostMapping("/restore")
	public String handleRestore(@RequestParam("backupFile") MultipartFile file, RedirectAttributes redirectAttributes) {
		// Kiểm tra xem người dùng đã chọn file hay chưa
		if (file.isEmpty()) {
			redirectAttributes.addFlashAttribute("error", "Vui lòng chọn file phục hồi!");
			return "redirect:/data/restore";
		}

		try {
			// TODO: Gọi logic phục hồi dữ liệu từ file (ví dụ: restore DB từ file SQL)
			System.out.println("Restore from file: " + file.getOriginalFilename());

			// Gửi thông báo thành công
			redirectAttributes.addFlashAttribute("success", "Phục hồi dữ liệu thành công!");
		} catch (Exception e) {
			// Gửi thông báo lỗi nếu xảy ra exception
			redirectAttributes.addFlashAttribute("error", "Đã có lỗi xảy ra khi phục hồi!");
		}

		return "redirect:/data/restore";
	}
}
