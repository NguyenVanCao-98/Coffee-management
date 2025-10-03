package com.viettridao.cafe.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller xử lý các yêu cầu liên quan đến trang "Giới thiệu" của ứng dụng.
 */
@Controller
public class AboutController {

	/**
	 * Xử lý yêu cầu GET đến đường dẫn "/about".
	 * 
	 * Phương thức này thêm các thông tin mô tả ứng dụng vào model để hiển thị trên
	 * giao diện người dùng, bao gồm: - Tên ứng dụng - Phiên bản hiện tại - Tên nhà
	 * phát triển - Mô tả chức năng chính của hệ thống - Cam kết chất lượng và hiệu
	 * năng
	 *
	 * @param model Đối tượng Model được dùng để truyền dữ liệu sang view.
	 * @return Tên view "about" để hiển thị trang giới thiệu.
	 */
	@GetMapping("/about")
	public String showAboutPage(Model model) {
		model.addAttribute("appName", "Hệ thống quản lý quán cafe");
		model.addAttribute("version", "1.0.0");
		model.addAttribute("developer", "Nguyen Van Cao");
		model.addAttribute("description",
				"Phần mềm được thiết kế nhằm tối ưu hóa quy trình quản lý quán cafe — từ đặt bàn, quản lý tồn kho đến theo dõi doanh thu và chi phí. Giao diện thân thiện, dễ sử dụng, phù hợp cho cả quản lý và nhân viên.");
		model.addAttribute("quality",
				"Cam kết hiệu năng ổn định, bảo mật dữ liệu và liên tục cập nhật để mang lại trải nghiệm tốt nhất cho người dùng.");

		return "about";
	}
}
