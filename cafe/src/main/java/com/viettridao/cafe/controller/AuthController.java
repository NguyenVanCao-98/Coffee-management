package com.viettridao.cafe.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.viettridao.cafe.service.AuthService;

import lombok.RequiredArgsConstructor;

/**
 * Controller xử lý chức năng đăng nhập người dùng.
 */
@Controller 
@RequiredArgsConstructor 
public class AuthController {

	private final AuthService authService; // Service xử lý logic đăng nhập

	/**
	 * Hiển thị form đăng nhập cho người dùng.
	 * 
	 * @return tên view login.html để hiển thị form đăng nhập
	 */
	@GetMapping("/login")
	public String showLoginForm() {
		return "login"; // Trả về view tên "login" (ứng với file login.html)
	}

	/**
	 * Xử lý khi người dùng submit form đăng nhập (POST /login).
	 *
	 * @param username           Tên đăng nhập được gửi từ form
	 * @param password           Mật khẩu được gửi từ form
	 * @param redirectAttributes Dùng để truyền thông báo tạm thời (flash message)
	 *                           khi redirect
	 * @return Chuyển hướng đến /home nếu đăng nhập thành công, ngược lại về lại
	 *         /login
	 */
	@PostMapping("/login")
	public String login(@RequestParam String username, // Lấy tham số username từ request
			@RequestParam String password, // Lấy tham số password từ request
			RedirectAttributes redirectAttributes) { // Để truyền flash attributes khi redirect
		try {
			boolean result = authService.login(username, password); // Gọi service kiểm tra đăng nhập

			if (result) { // Nếu đăng nhập thành công
				redirectAttributes.addFlashAttribute("success", "Đăng nhập thành công!"); // Thêm thông báo thành công
				return "redirect:/home"; // Chuyển hướng tới trang chính /home
			}
		} catch (RuntimeException e) {
			// Nếu có lỗi xảy ra (ví dụ tên đăng nhập hoặc mật khẩu không đúng)
			redirectAttributes.addFlashAttribute("error", e.getMessage()); // Truyền thông báo lỗi qua flash attribute
			return "redirect:/login"; // Quay lại trang login
		}

		// Nếu đăng nhập thất bại nhưng không có exception, trả về lỗi chung
		redirectAttributes.addFlashAttribute("error", "Đăng nhập thất bại");
		return "redirect:/login"; // Quay lại trang login
	}
}
