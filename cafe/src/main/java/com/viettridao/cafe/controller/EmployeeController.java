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

import com.viettridao.cafe.dto.request.employee.CreateEmployeeRequest;
import com.viettridao.cafe.dto.request.employee.UpdateEmployeeRequest;
import com.viettridao.cafe.dto.response.employee.EmployeeResponse;
import com.viettridao.cafe.mapper.EmployeeMapper;
import com.viettridao.cafe.mapper.PositionMapper;
import com.viettridao.cafe.service.EmployeeService;
import com.viettridao.cafe.service.PositionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controller quản lý các thao tác CRUD liên quan đến nhân viên (Employee)
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/employee")
public class EmployeeController {

	private final EmployeeService employeeService;
	private final EmployeeMapper employeeMapper;
	private final PositionService positionService;
	private final PositionMapper positionMapper;

	/**
	 * Hiển thị danh sách nhân viên với phân trang và tìm kiếm.
	 *
	 * @param keyword từ khoá tìm kiếm (có thể null)
	 * @param page    số trang hiện tại (mặc định 0)
	 * @param size    số bản ghi trên mỗi trang (mặc định 5)
	 * @param model   đối tượng để truyền dữ liệu sang view
	 */
	@GetMapping("")
	public String home(@RequestParam(required = false) String keyword, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size, Model model) {
		model.addAttribute("employees", employeeService.getAllEmployees(keyword, page, size));
		return "/employees/employee";
	}

	/**
	 * Hiển thị form thêm mới nhân viên.
	 *
	 * @param model truyền danh sách vị trí và object request rỗng
	 */
	@GetMapping("/create")
	public String showFormCreate(Model model) {
		if (!model.containsAttribute("employee")) {
			model.addAttribute("employee", new CreateEmployeeRequest());
		}
		model.addAttribute("positions", positionMapper.toDtoList(positionService.getPositions()));
		return "/employees/create_employee";
	}

	/**
	 * Xử lý việc tạo nhân viên mới từ form.
	 *
	 * @param employee           thông tin nhân viên cần tạo
	 * @param result             kết quả validate
	 * @param redirectAttributes để gửi flash message
	 * @param model              trả lại data khi có lỗi
	 */
	@PostMapping("/create")
	public String createEmployee(@Valid @ModelAttribute("employee") CreateEmployeeRequest employee,
			BindingResult result, Model model, RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			model.addAttribute("positions", positionMapper.toDtoList(positionService.getPositions()));
			return "/employees/create_employee"; 
		}
		try {
			employeeService.createEmployee(employee);
			redirectAttributes.addFlashAttribute("success", "Thêm nhân viên thành công");
			return "redirect:/employee";
		} catch (Exception e) {
			model.addAttribute("positions", positionMapper.toDtoList(positionService.getPositions()));
			model.addAttribute("error", e.getMessage());
			return "/employees/create_employee";
		}
	}

	/**
	 * Xử lý yêu cầu xoá nhân viên theo ID.
	 *
	 * @param id                 ID của nhân viên cần xoá
	 * @param redirectAttributes flash message kết quả xử lý
	 */
	@PostMapping("/delete/{id}")
	public String deleteEmployee(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
		try {
			employeeService.deleteEmployee(id);
			redirectAttributes.addFlashAttribute("success", "Xoá nhân viên thành công");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
		}
		return "redirect:/employee";
	}

	/**
	 * Hiển thị form cập nhật thông tin nhân viên theo ID.
	 *
	 * @param id                 ID của nhân viên
	 * @param model              dùng để truyền dữ liệu ra view
	 * @param redirectAttributes flash message nếu có lỗi
	 */
	@GetMapping("/update/{id}")
	public String showFormUpdate(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
		try {
			EmployeeResponse response = employeeMapper.toDto(employeeService.getEmployeeById(id));
			model.addAttribute("positions", positionMapper.toDtoList(positionService.getPositions()));
			model.addAttribute("employee", response);
			return "/employees/update_employee";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			return "redirect:/employee";
		}
	}

	/**
	 * Xử lý cập nhật thông tin nhân viên.
	 *
	 * @param request            DTO chứa dữ liệu cập nhật
	 * @param result             validate dữ liệu
	 * @param redirectAttributes flash message sau khi xử lý
	 * @param model              truyền lại dữ liệu nếu validate fail
	 */
	@PostMapping("/update")
	public String updateEmployee(@Valid @ModelAttribute UpdateEmployeeRequest request, BindingResult result,
			RedirectAttributes redirectAttributes, Model model) {
		try {
			if (result.hasErrors()) {
				model.addAttribute("positions", positionMapper.toDtoList(positionService.getPositions()));
				return "/employees/update_employee";
			}

			employeeService.updateEmployee(request);
			redirectAttributes.addFlashAttribute("success", "Chỉnh sửa nhân viên thành công");
			return "redirect:/employee";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			return "redirect:/employee";
		}
	}
}
