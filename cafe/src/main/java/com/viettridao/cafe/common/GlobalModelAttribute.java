package com.viettridao.cafe.common;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.viettridao.cafe.model.AccountEntity;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Lớp GlobalModelAttribute dùng để cung cấp các biến dùng chung cho toàn bộ
 * controller trong ứng dụng. Các biến này sẽ tự động được thêm vào model của
 * tất cả các request mà không cần khai báo lại.
 */
@ControllerAdvice
public class GlobalModelAttribute {

	/**
	 * Gán đường dẫn hiện tại (URI) của request vào model với tên "currentPath".
	 * Giúp view (ví dụ: Thymeleaf) có thể hiển thị hay so sánh đường dẫn hiện tại
	 * cho các mục đích như active menu, breadcrumb...
	 *
	 * @param request Đối tượng HttpServletRequest hiện tại.
	 * @return Chuỗi đại diện cho URI của request.
	 */
	@ModelAttribute("currentPath")
	public String populateCurrentPath(HttpServletRequest request) {
		return request.getRequestURI();
	}

	/**
	 * Gán thông tin người dùng hiện tại (đã đăng nhập) vào model với tên "user".
	 * Nếu người dùng chưa đăng nhập (ẩn danh), sẽ trả về null.
	 *
	 * @return Đối tượng AccountEntity đại diện cho người dùng đã đăng nhập, hoặc
	 *         null nếu chưa đăng nhập.
	 */
	@ModelAttribute("user")
	public AccountEntity addUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		// Kiểm tra xem người dùng đã xác thực và không phải là anonymous
		if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
			return (AccountEntity) auth.getPrincipal();
		}

		return null;
	}
}
