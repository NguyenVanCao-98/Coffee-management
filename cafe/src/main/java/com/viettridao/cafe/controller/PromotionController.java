package com.viettridao.cafe.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.viettridao.cafe.dto.request.promotion.CreatePromotionRequest;
import com.viettridao.cafe.dto.request.promotion.UpdatePromotionRequest;
import com.viettridao.cafe.dto.response.promotion.PromotionResponse;
import com.viettridao.cafe.mapper.PromotionMapper;
import com.viettridao.cafe.service.PromotionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controller quản lý các thao tác CRUD liên quan đến khuyến mãi (promotion).
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/promotion")
public class PromotionController {

	private final PromotionService promotionService;
	private final PromotionMapper promotionMapper;

	/**
	 * Hiển thị danh sách khuyến mãi có phân trang.
	 * 
	 * @param page  trang hiện tại (mặc định là 0)
	 * @param size  số lượng phần tử mỗi trang (mặc định là 5)
	 * @param model đối tượng Model dùng để truyền dữ liệu sang view
	 * @return tên view hiển thị danh sách khuyến mãi
	 */
	@GetMapping("")
	public String home(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size,
			Model model) {
		model.addAttribute("promotions", promotionService.getAllPromotions(page, size));
		return "/promotions/promotion";
	}

	/**
	 * Hiển thị form tạo khuyến mãi mới.
	 * 
	 * @param model đối tượng Model để binding dữ liệu với form
	 * @return tên view hiển thị form tạo khuyến mãi
	 */
	@GetMapping("/create")
	public String showFormCreate(Model model) {
		model.addAttribute("promotion", new CreatePromotionRequest());
		return "/promotions/create_promotion";
	}

	/**
	 * Xử lý tạo khuyến mãi mới sau khi submit form.
	 * 
	 * @param promotion          đối tượng chứa dữ liệu nhập từ form
	 * @param result             kết quả kiểm tra hợp lệ (validation)
	 * @param redirectAttributes thuộc tính chuyển tiếp thông báo
	 * @return chuyển hướng về danh sách hoặc hiển thị lại form nếu lỗi
	 */
	@PostMapping("/create")
	public String createPromotion(@Valid @ModelAttribute("promotion") CreatePromotionRequest promotion,
			BindingResult result, RedirectAttributes redirectAttributes) {
		try {
			if (result.hasErrors()) {
				return "/promotions/create_promotion";
			}
			promotionService.createPromotion(promotion);
			redirectAttributes.addFlashAttribute("success", "Thêm khuyến mãi thành công");
			return "redirect:/promotion";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			return "redirect:/promotion/create";
		}
	}

	/**
	 * Xử lý xóa một khuyến mãi theo ID.
	 * 
	 * @param id                 ID của khuyến mãi cần xóa
	 * @param redirectAttributes thuộc tính chuyển tiếp thông báo
	 * @return chuyển hướng về trang danh sách khuyến mãi
	 */
	@PostMapping("/delete/{id}")
		public String deletePromotion(@PathVariable("id") Integer id,
		                              @RequestParam(value = "page", defaultValue = "0") int page,
		                              RedirectAttributes redirectAttributes) {
		    try {
		        promotionService.deletePromotion(id);
		        redirectAttributes.addFlashAttribute("success", "Xoá khuyến mãi thành công");
		        return "redirect:/promotion?page=" + page; // redirect về trang hiện tại
		    } catch (Exception e) {
		        redirectAttributes.addFlashAttribute("error", e.getMessage());
		        return "redirect:/promotion?page=" + page;
		    }
		}


	/**
	 * Hiển thị form cập nhật thông tin khuyến mãi theo ID.
	 * 
	 * @param id                 ID khuyến mãi cần cập nhật
	 * @param model              Model để binding dữ liệu
	 * @param redirectAttributes thuộc tính chuyển tiếp thông báo
	 * @return tên view form cập nhật hoặc redirect nếu lỗi
	 */
	@GetMapping("/update/{id}")
	public String showFormUpdate(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
		try {
			PromotionResponse response = promotionMapper.toDto(promotionService.getPromotionById(id));
			model.addAttribute("promotion", response);
			return "/promotions/update_promotion";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			return "redirect:/promotion";
		}
	}

	/**
	 * Xử lý cập nhật thông tin khuyến mãi.
	 * 
	 * @param request            dữ liệu cập nhật từ form
	 * @param result             kết quả kiểm tra hợp lệ
	 * @param redirectAttributes thuộc tính chuyển tiếp thông báo
	 * @return chuyển hướng về danh sách hoặc hiển thị lại form nếu có lỗi
	 */
	@PostMapping("/update")
	public String updatePromotion(@Valid @ModelAttribute UpdatePromotionRequest request, BindingResult result,
			RedirectAttributes redirectAttributes) {
		try {
			if (result.hasErrors()) {
				return "/promotions/update_promotion";
			}
			promotionService.updatePromotion(request);
			redirectAttributes.addFlashAttribute("success", "Chỉnh sửa khuyến mãi thành công");
			return "redirect:/promotion";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			return "redirect:/promotion";
		}
	}
}
