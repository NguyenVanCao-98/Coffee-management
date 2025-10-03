package com.viettridao.cafe.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.viettridao.cafe.dto.request.tables.TableBookingRequest;
import com.viettridao.cafe.dto.response.tables.TableBookingResponse;
import com.viettridao.cafe.model.EmployeeEntity;
import com.viettridao.cafe.repository.EmployeeRepository;
import com.viettridao.cafe.service.ReservationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controller xử lý nghiệp vụ liên quan đến việc đặt bàn trong hệ thống.
 */
@Controller
@RequestMapping("/booking")
@RequiredArgsConstructor
public class ReservationController {

	// Khai báo đối tượng service để xử lý logic đặt bàn
	private final ReservationService reservationService;

	// Khai báo repository để truy vấn thông tin nhân viên từ cơ sở dữ liệu
	private final EmployeeRepository employeeRepository;

	/**
	 * Hiển thị form đặt bàn. Lấy thông tin nhân viên hiện tại từ Principal và truyền vào model.
	 *
	 * @param tableId   ID của bàn cần đặt
	 * @param model     Đối tượng model để truyền dữ liệu sang view
	 * @param principal Thông tin người dùng hiện tại đã đăng nhập
	 * @return Trang form đặt bàn
	 */
	@GetMapping
	public String showBookingForm(@RequestParam("tableId") Integer tableId, Model model, Principal principal) {
		// Tạo request rỗng để binding dữ liệu đặt bàn
		TableBookingRequest request = new TableBookingRequest();

		// Gán ID bàn cần đặt vào request
		request.setTableId(tableId);

		try {
			// Lấy username của người dùng đang đăng nhập
			String username = principal.getName();

			// Tìm thông tin nhân viên theo username
			EmployeeEntity employee = employeeRepository.findByAccountUsername(username)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên với username: " + username));

			// Gán ID nhân viên vào request đặt bàn
			request.setEmployeeId(employee.getId());

			// Truyền tên nhân viên vào model để hiển thị ở view
			model.addAttribute("employeeName", employee.getFullName());

		} catch (Exception ex) {
			// Trong trường hợp lỗi xảy ra, gán thông báo lỗi vào model
			model.addAttribute("error", "Không thể lấy thông tin nhân viên.");

			// Gán lại request để giữ thông tin người dùng đã nhập
			model.addAttribute("booking", request);
			
		    model.addAttribute("today", java.time.LocalDate.now()); 


			// Trả về view form đặt bàn (kèm theo lỗi)
			return "booking/form";
		}

		// Truyền request (chứa thông tin đặt bàn) sang view
		model.addAttribute("booking", request);
		
	    model.addAttribute("today", java.time.LocalDate.now()); 

		// Trả về view form đặt bàn
		return "booking/form";
	}

	/**
	 * Xử lý yêu cầu đặt bàn từ form. Kiểm tra validate và lưu thông tin đặt bàn thông qua service.
	 *
	 * @param request            Dữ liệu yêu cầu đặt bàn
	 * @param result             Kết quả kiểm tra validate
	 * @param model              Model để truyền dữ liệu sang view
	 * @param principal          Người dùng hiện tại
	 * @param redirectAttributes Đối tượng để gửi thông báo khi redirect
	 * @return Redirect sang trang bán hàng nếu thành công, hoặc quay lại form nếu thất bại
	 */
	@PostMapping
	public String bookTable(@ModelAttribute("booking") @Valid TableBookingRequest request, BindingResult result,
		Model model, Principal principal, RedirectAttributes redirectAttributes) {

		// Nếu dữ liệu nhập vào không hợp lệ (sai validate), quay lại form đặt bàn
		if (result.hasErrors()) {
			return "booking/form";
		}

		try {
			// Lấy username của người đang đăng nhập
			String username = principal.getName();

			// Truy xuất thông tin nhân viên theo username
			EmployeeEntity employee = employeeRepository.findByAccountUsername(username)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên."));

			// Gán ID nhân viên vào request
			request.setEmployeeId(employee.getId());

			// Gán tên nhân viên vào model để hiển thị ở view
			model.addAttribute("employeeName", employee.getFullName());

			// Gọi service để thực hiện đặt bàn
			TableBookingResponse response = reservationService.bookTable(request);

			// Nếu service trả về không thành công
			if (!response.isSuccess()) {
				// Truyền thông báo lỗi vào model
				model.addAttribute("error", response.getMessage());

				// Quay lại form đặt bàn
				return "booking/form";
			}

			// Nếu đặt bàn thành công, truyền thông báo và chuyển hướng đến trang sale
			redirectAttributes.addFlashAttribute("success", "Đặt bàn thành công.");
			return "redirect:/sale";

		} catch (Exception e) {
			// Nếu xảy ra lỗi hệ thống, truyền thông báo lỗi và quay lại form
			model.addAttribute("error", "Đã xảy ra lỗi trong quá trình đặt bàn.");
			return "booking/form";
		}
	}
}