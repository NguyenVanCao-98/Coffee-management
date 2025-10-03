package com.viettridao.cafe.controller;

import java.time.LocalDate;

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

import com.viettridao.cafe.dto.request.equipment.CreateEquipmentRequest;
import com.viettridao.cafe.dto.request.equipment.UpdateEquipmentRequest;
import com.viettridao.cafe.dto.response.equipment.EquipmentResponse;
import com.viettridao.cafe.mapper.EquipmentMapper;
import com.viettridao.cafe.service.EquipmentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controller quản lý các thao tác CRUD đối với thiết bị trong hệ thống.
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/equipment")
public class EquipmentController {

	private final EquipmentService equipmentService; // Service chứa logic xử lý thiết bị
	private final EquipmentMapper equipmentMapper; // Mapper chuyển đổi entity <-> DTO

	/**
	 * Hiển thị danh sách thiết bị với phân trang.
	 * 
	 * @param page  trang hiện tại, mặc định 0
	 * @param size  số lượng bản ghi trên mỗi trang, mặc định 5
	 * @param model Model chứa dữ liệu truyền sang view
	 * @return tên view danh sách thiết bị
	 */
	@GetMapping("")
	public String home(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size,
			Model model) {
		// Lấy danh sách thiết bị phân trang rồi thêm vào model với tên "equiments"
		model.addAttribute("equiments", equipmentService.getAllEquipmentsPage(page, size));
		// Trả về view hiển thị danh sách thiết bị
		return "/equipments/equipment";
	}

	/**
	 * Hiển thị form thêm mới thiết bị.
	 * 
	 * @param model chứa dữ liệu gửi sang view
	 * @return tên view form tạo thiết bị mới
	 */
	@GetMapping("/create")
	public String showFormCreate(Model model) {
	    CreateEquipmentRequest equipment = new CreateEquipmentRequest();
	    equipment.setPurchaseDate(LocalDate.now()); 
	    model.addAttribute("equipment", equipment); 
	    return "/equipments/create_equipment";
	}

	/**
	 * Xử lý yêu cầu tạo mới thiết bị.
	 * 
	 * @param equipment          dữ liệu thiết bị mới được gửi từ form, được
	 *                           validate
	 * @param result             kết quả validate
	 * @param redirectAttributes để truyền flash message khi redirect
	 * @return redirect về danh sách thiết bị hoặc trả lại form nếu lỗi
	 */
	@PostMapping("/create")
	public String createEquipment(@Valid @ModelAttribute("equipment") CreateEquipmentRequest equipment,
			BindingResult result, RedirectAttributes redirectAttributes) {
		try {
			// Nếu có lỗi validate, trả lại view form tạo thiết bị với lỗi hiển thị
			if (result.hasErrors()) {
				return "/equipments/create_equipment";
			}

			// Gọi service tạo thiết bị mới
			equipmentService.createEquipment(equipment);
			// Thêm flash message thành công
			redirectAttributes.addFlashAttribute("success", "Thêm thiết bị thành công");
			// Redirect về trang danh sách thiết bị
			return "redirect:/equipment";
		} catch (Exception e) {
			// Nếu lỗi xảy ra khi tạo, thêm flash message lỗi
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			// Redirect về lại form tạo thiết bị
			return "redirect:/equipment/create";
		}
	}

	/**
	 * Xử lý yêu cầu xoá thiết bị theo ID.
	 * 
	 * @param id                 ID thiết bị cần xoá
	 * @param redirectAttributes để truyền flash message khi redirect
	 * @return redirect về danh sách thiết bị
	 */
	@PostMapping("/delete/{id}")
	public String deleteDevice(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
		try {
			// Gọi service xoá thiết bị theo id
			equipmentService.deleteEquipment(id);
			// Thêm flash message thành công
			redirectAttributes.addFlashAttribute("success", "Xoá thiết bị thành công");
		} catch (Exception e) {
			// Nếu lỗi xảy ra khi xoá, thêm flash message lỗi
			redirectAttributes.addFlashAttribute("error", e.getMessage());
		}
		// Redirect về trang danh sách thiết bị
		return "redirect:/equipment";
	}

	/**
	 * Hiển thị form cập nhật thông tin thiết bị.
	 * 
	 * @param id                 ID thiết bị cần chỉnh sửa
	 * @param model              chứa dữ liệu truyền sang view
	 * @param redirectAttributes để truyền flash message khi redirect
	 * @return view form cập nhật thiết bị hoặc redirect về danh sách nếu lỗi
	 */
	@GetMapping("/update/{id}")
	public String showFormUpdate(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
		try {
			// Lấy thông tin thiết bị theo id, chuyển thành DTO
			EquipmentResponse response = equipmentMapper.toDto(equipmentService.getEquipmentById(id));
			// Thêm thiết bị vào model để binding dữ liệu lên form
			model.addAttribute("equipment", response);
			// Trả về view form cập nhật thiết bị
			return "/equipments/update_equipment";
		} catch (Exception e) {
			// Nếu lỗi xảy ra, thêm flash message lỗi
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			// Redirect về trang danh sách thiết bị
			return "redirect:/equipment";
		}
	}

	/**
	 * Xử lý yêu cầu cập nhật thông tin thiết bị.
	 * 
	 * @param request            dữ liệu thiết bị mới được gửi từ form, được
	 *                           validate
	 * @param result             kết quả validate
	 * @param redirectAttributes để truyền flash message khi redirect
	 * @return redirect về danh sách thiết bị hoặc trả lại form nếu lỗi
	 */
	@PostMapping("/update")
	public String updateEquipment(@Valid @ModelAttribute UpdateEquipmentRequest request, BindingResult result,
			RedirectAttributes redirectAttributes) {
		try {
			// Nếu có lỗi validate, trả lại view form cập nhật thiết bị
			if (result.hasErrors()) {
				return "/equipments/update_equipment";
			}

			// Gọi service cập nhật thiết bị
			equipmentService.updateEquipment(request);
			// Thêm flash message thành công
			redirectAttributes.addFlashAttribute("success", "Chỉnh sửa thiết bị thành công");
			// Redirect về trang danh sách thiết bị
			return "redirect:/equipment";
		} catch (Exception e) {
			// Nếu lỗi xảy ra, thêm flash message lỗi
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			// Redirect về trang danh sách thiết bị
			return "redirect:/equipment";
		}
	}
}
