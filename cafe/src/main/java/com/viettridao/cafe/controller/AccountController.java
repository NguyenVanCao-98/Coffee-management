package com.viettridao.cafe.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.viettridao.cafe.dto.request.account.UpdateAccountRequest;
import com.viettridao.cafe.dto.response.account.AccountResponse;
import com.viettridao.cafe.mapper.AccountMapper;
import com.viettridao.cafe.service.AccountService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controller xử lý các chức năng liên quan đến tài khoản người dùng, bao gồm
 * hiển thị và cập nhật thông tin cá nhân.
 */
@Controller 
@RequiredArgsConstructor 
@RequestMapping("/account") 
public class AccountController {

	private final AccountService accountService; // Service xử lý nghiệp vụ liên quan tài khoản
	private final AccountMapper accountMapper; // Mapper chuyển đổi entity <-> DTO

	/**
	 * Xử lý GET request tới /account Hiển thị trang thông tin tài khoản người dùng
	 * hiện tại.
	 */
	@GetMapping("")
	public String home(Model model) {
		// Nếu model chưa có attribute "account" (chẳng hạn khi redirect từ update bị
		// lỗi)
		if (!model.containsAttribute("account")) {
			// Lấy thông tin Authentication của user đang đăng nhập từ SecurityContext
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();

			// Lấy thông tin tài khoản theo username hiện tại, chuyển thành DTO để trả về
			// view
			AccountResponse accountResponse = accountMapper.toDto(accountService.getAccountByUsername(auth.getName()));

			// Thêm accountResponse vào model để view có dữ liệu hiển thị
			model.addAttribute("account", accountResponse != null ? accountResponse : new AccountResponse());
		}

		// Trả về tên view (template) hiển thị thông tin tài khoản
		return "/accounts/account";
	}

	/**
	 * Xử lý POST request cập nhật thông tin tài khoản tại /account/update
	 * 
	 * @param request            DTO chứa dữ liệu update, được validate (@Valid)
	 * @param bindingResult      Kết quả validate dữ liệu
	 * @param redirectAttributes Dùng để truyền attribute khi redirect
	 * @return redirect về trang /account sau khi xử lý
	 */
	@PostMapping("/update")
	public String updateAccount(@Valid @ModelAttribute("account") UpdateAccountRequest request, // Dữ liệu form update
																								// account, tự động bind
			BindingResult bindingResult, // Kiểm tra lỗi validate
			RedirectAttributes redirectAttributes // Truyền dữ liệu flash khi redirect
	) {
		// Nếu có lỗi validate, truyền lỗi và dữ liệu form qua flash attribute rồi
		// redirect về lại trang /account
		if (bindingResult.hasErrors()) {
			redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.account", bindingResult);
			redirectAttributes.addFlashAttribute("account", request);

			return "redirect:/account";
		}

		try {
			// Gọi service cập nhật thông tin tài khoản
			accountService.updateAccount(request);

			// Nếu thành công, thêm thông báo thành công vào flash attribute
			redirectAttributes.addFlashAttribute("success", "Cập nhật thông tin cá nhân thành công!");
		} catch (Exception e) {
			// Nếu lỗi xảy ra khi cập nhật, thêm thông báo lỗi và dữ liệu form vào flash
			// attribute
			redirectAttributes.addFlashAttribute("error", "Cập nhật thất bại: " + e.getMessage());
			redirectAttributes.addFlashAttribute("account", request);
		}

		// Sau cùng redirect về trang /account để hiển thị kết quả
		return "redirect:/account";
	}
}
