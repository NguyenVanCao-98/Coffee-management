package com.viettridao.cafe.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.viettridao.cafe.dto.request.invoices.InvoiceItemListRequest;
import com.viettridao.cafe.dto.request.invoices.InvoiceItemRequest;
import com.viettridao.cafe.model.MenuItemEntity;
import com.viettridao.cafe.repository.MenuItemRepository;
import com.viettridao.cafe.service.InvoiceItemService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/invoice")
@RequiredArgsConstructor
public class InvoiceItemController {

	private final InvoiceItemService invoiceItemService; // Service xử lý nghiệp vụ liên quan món trong hóa đơn
	private final MenuItemRepository menuItemRepository; // Repository truy xuất dữ liệu món ăn từ DB

	/**
	 * Hiển thị form thêm món cho hóa đơn theo invoiceId.
	 * 
	 * @param invoiceId id hóa đơn truyền vào từ request param
	 * @param model     đối tượng model truyền dữ liệu sang view
	 * @return view form thêm món "sale/item-form"
	 */
	@GetMapping("/item-form")
	public String showItemForm(@RequestParam("invoiceId") Integer invoiceId, Model model) {
		// Lấy danh sách món chưa bị xoá (isDeleted=false)
		List<MenuItemEntity> menuItems = menuItemRepository.findByIsDeletedFalse();

		// Tạo list các request item mặc định với số lượng = 0
		List<InvoiceItemRequest> itemRequests = new ArrayList<>();
		for (MenuItemEntity item : menuItems) {
			InvoiceItemRequest req = new InvoiceItemRequest();
			req.setInvoiceId(invoiceId); // Gán id hóa đơn
			req.setMenuItemId(item.getId()); // Gán id món ăn
			req.setQuantity(0); // Mặc định số lượng = 0
			itemRequests.add(req);
		}

		// Tạo đối tượng listRequest chứa danh sách các item request trên
		InvoiceItemListRequest listRequest = new InvoiceItemListRequest();
		listRequest.setItems(itemRequests);

		// Đưa danh sách món ăn và form request vào model để hiển thị lên view
		model.addAttribute("menuItems", menuItems);
		model.addAttribute("form", listRequest);

		// Trả về view hiển thị form thêm món cho hóa đơn
		return "sale/item-form";
	}

	/**
	 * Xử lý POST request thêm món cho hóa đơn.
	 * 
	 * @param request            dữ liệu danh sách món được submit từ form, đã được
	 *                           validate
	 * @param result             chứa kết quả validate dữ liệu
	 * @param model              để truyền dữ liệu sang view khi có lỗi
	 * @param redirectAttributes dùng để truyền flash message khi redirect
	 * @return redirect về /sale nếu thành công hoặc trả lại form nếu có lỗi
	 */
	@PostMapping("/item")
	public String addItems(@ModelAttribute("form") @Valid InvoiceItemListRequest request, BindingResult result,
			Model model, RedirectAttributes redirectAttributes) {

		// Kiểm tra trùng món: dùng Set để phát hiện menuItemId bị chọn nhiều lần
		Set<Integer> menuItemIds = new HashSet<>();
		for (int i = 0; i < request.getItems().size(); i++) {
			InvoiceItemRequest item = request.getItems().get(i);
			if (!menuItemIds.add(item.getMenuItemId())) {
				// Nếu phát hiện trùng, reject lỗi với vị trí phần tử i
				result.rejectValue("items[" + i + "].menuItemId", null, "Không được chọn trùng món ăn.");
			}
		}

		// Kiểm tra ít nhất 1 món có số lượng > 0
		boolean hasValidItem = request.getItems().stream()
				.anyMatch(i -> i.getQuantity() != null && i.getQuantity() > 0);
		if (!hasValidItem) {
			result.rejectValue("items", null, "Phải chọn ít nhất một món với số lượng > 0.");
		}

		// Nếu có lỗi validate, trả lại form kèm dữ liệu và lỗi
		if (result.hasErrors()) {
			model.addAttribute("menuItems", menuItemRepository.findByIsDeletedFalse());
			return "sale/item-form";
		}

		try {
			// Gọi service thêm các món vào hóa đơn
			invoiceItemService.addItemsToInvoice(request);
			// Thêm flash message thành công
			redirectAttributes.addFlashAttribute("success", "Thêm món thành công!");
			// Redirect về trang danh sách bán hàng
			return "redirect:/sale";
		} catch (Exception e) {
			// Nếu lỗi xảy ra, hiển thị lỗi và trả lại form
			model.addAttribute("error", "Đã xảy ra lỗi khi thêm món vào hóa đơn.");
			model.addAttribute("menuItems", menuItemRepository.findByIsDeletedFalse());
			return "sale/item-form";
		}
	}
}
