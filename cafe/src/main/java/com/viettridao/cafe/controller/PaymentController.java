package com.viettridao.cafe.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.viettridao.cafe.dto.request.Pay.PaymentItemRequest;
import com.viettridao.cafe.dto.request.Pay.PaymentRequest;
import com.viettridao.cafe.dto.response.Pay.PaymentResponse;
import com.viettridao.cafe.dto.response.tables.TableMenuItemResponse;
import com.viettridao.cafe.model.ReservationEntity;
import com.viettridao.cafe.service.PaymentService;
import com.viettridao.cafe.service.TableService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controller xử lý các chức năng thanh toán hóa đơn cho bàn.
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/payment")
public class PaymentController {

	// Khai báo TableService để lấy dữ liệu bàn và món ăn liên quan
	private final TableService tableService;

	// Khai báo PaymentService để xử lý logic thanh toán
	private final PaymentService paymentService;

	/**
	 * Hiển thị form thanh toán cho bàn được chọn.
	 * 
	 * @param tableId ID của bàn
	 * @param model   Model để truyền dữ liệu sang view
	 * @return Trang hiển thị form thanh toán
	 */
	@GetMapping("/{tableId}")
	public String showPaymentForm(@PathVariable Integer tableId, Model model) {
		// Lấy danh sách món ăn của bàn theo ID
		List<TableMenuItemResponse> items = tableService.getTableMenuItems(tableId);

		// Tính tổng tiền các món trong hóa đơn
		double total = items.stream()
			.mapToDouble(item -> item.getAmount() != null ? item.getAmount() : 0.0)
			.sum();

		// Tạo đối tượng PaymentRequest để truyền dữ liệu thanh toán
		PaymentRequest request = new PaymentRequest();
		request.setTableId(tableId); // Gán ID bàn
		request.setCustomerCash(0); // Mặc định tiền khách trả là 0
		request.setFreeTable(true); // Mặc định sau thanh toán thì bàn trống

		// Gán thông tin món ăn vào request
		List<PaymentItemRequest> itemRequests = items.stream().map(item -> {
			// Tạo đối tượng từng món cần thanh toán
			PaymentItemRequest reqItem = new PaymentItemRequest();
			reqItem.setMenuItemId(item.getMenuItemId()); // ID món
			reqItem.setQuantity(item.getQuantity()); // Số lượng
			reqItem.setPrice(item.getPrice()); // Đơn giá
			reqItem.setAmount(item.getAmount()); // Thành tiền
			return reqItem; // Trả về request item
		}).toList();

		// Gán danh sách món vào request
		request.setItems(itemRequests);

		// Lấy thông tin nhân viên phục vụ từ lịch sử đặt bàn
		ReservationEntity reservation = tableService.getLatestReservationByTableId(tableId);
		String employeeName = (reservation != null && reservation.getEmployee() != null)
			? reservation.getEmployee().getFullName() // Lấy tên nhân viên
			: "Không xác định"; // Nếu không có thì hiển thị mặc định

		// Đưa dữ liệu ra view
		model.addAttribute("tableId", tableId); // Truyền ID bàn
		model.addAttribute("menuItems", items); // Truyền danh sách món ăn
		model.addAttribute("totalAmount", total); // Truyền tổng tiền
		model.addAttribute("employeeName", employeeName); // Truyền tên nhân viên
		model.addAttribute("paymentRequest", request); // Truyền request thanh toán

		// Trả về view hiển thị form thanh toán
		return "sale/form";
	}

	/**
	 * Xử lý yêu cầu thanh toán từ form.
	 * 
	 * @param request       Dữ liệu thanh toán gửi từ form
	 * @param bindingResult Kết quả kiểm tra hợp lệ của form
	 * @param model         Model để trả dữ liệu về view nếu có lỗi
	 * @return Trang kết quả hoặc trở về form nếu có lỗi
	 */
	@PostMapping("/process")
	public String processPayment(@Valid @ModelAttribute("paymentRequest") PaymentRequest request,
		BindingResult bindingResult, Model model) {

		// Nếu dữ liệu không hợp lệ, quay lại form và giữ dữ liệu
		if (bindingResult.hasErrors()) {
			return prepareFormOnError(request, model, "sale/form");
		}

		try {
			// Gọi service để xử lý logic thanh toán
			PaymentResponse response = paymentService.processPayment(request);

			// Nếu thanh toán thất bại, hiển thị lỗi và trở lại form
			if (!Boolean.TRUE.equals(response.isSuccess())) {
				model.addAttribute("error", response.getMessage());
				return prepareFormOnError(request, model, "sale/form");
			}

			// Nếu thanh toán thành công, hiển thị trang kết quả
			model.addAttribute("response", response); // Truyền response để hiển thị thông tin
			model.addAttribute("success", response.getMessage()); // Thông báo thành công
			model.addAttribute("tableId", request.getTableId()); // Truyền ID bàn

			// Trả về view kết quả thanh toán
			return "sale/result";

		} catch (Exception e) {
			// Nếu xảy ra lỗi hệ thống, hiển thị lỗi và trở lại form
			model.addAttribute("error", "Đã xảy ra lỗi khi xử lý thanh toán.");
			return prepareFormOnError(request, model, "sale/form");
		}
	}

	/**
	 * Hàm hỗ trợ chuẩn bị lại form khi xảy ra lỗi (ví dụ validate hoặc hệ thống).
	 * 
	 * @param request PaymentRequest cần phục hồi dữ liệu
	 * @param model   Model để đổ dữ liệu về view
	 * @param view    Tên view cần hiển thị lại
	 * @return view cần trả về
	 */
	private String prepareFormOnError(PaymentRequest request, Model model, String view) {
		// Lấy lại danh sách món ăn từ bàn
		List<TableMenuItemResponse> items = tableService.getTableMenuItems(request.getTableId());

		// Tính lại tổng tiền
		double total = items.stream()
			.mapToDouble(item -> item.getAmount() != null ? item.getAmount() : 0.0)
			.sum();

		// Gán danh sách món ăn vào request
		List<PaymentItemRequest> itemRequests = items.stream().map(item -> {
			PaymentItemRequest reqItem = new PaymentItemRequest();
			reqItem.setMenuItemId(item.getMenuItemId());
			reqItem.setQuantity(item.getQuantity());
			reqItem.setPrice(item.getPrice());
			reqItem.setAmount(item.getAmount());
			return reqItem;
		}).toList();
		request.setItems(itemRequests);

		// Lấy lại tên nhân viên phục vụ
		ReservationEntity reservation = tableService.getLatestReservationByTableId(request.getTableId());
		String employeeName = (reservation != null && reservation.getEmployee() != null)
			? reservation.getEmployee().getFullName()
			: "Không xác định";

		// Truyền lại dữ liệu cho view
		model.addAttribute("tableId", request.getTableId());
		model.addAttribute("menuItems", items);
		model.addAttribute("totalAmount", total);
		model.addAttribute("employeeName", employeeName);
		model.addAttribute("paymentRequest", request);

		// Trả về view ban đầu
		return view;
	}
}