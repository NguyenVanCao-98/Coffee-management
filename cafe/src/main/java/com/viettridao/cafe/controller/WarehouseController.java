package com.viettridao.cafe.controller;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.viettridao.cafe.dto.request.export.ExportRequest;
import com.viettridao.cafe.dto.request.imports.ImportRequest;
import com.viettridao.cafe.dto.request.product.ProductRequest;
import com.viettridao.cafe.dto.response.product.ProductResponse;
import com.viettridao.cafe.service.ExportService;
import com.viettridao.cafe.service.ImportService;
import com.viettridao.cafe.service.ProductService;
import com.viettridao.cafe.service.UnitService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * WarehouseController quản lý các chức năng liên quan đến kho hàng: - Hiển thị
 * danh sách sản phẩm - Thêm mới sản phẩm qua nhập hàng - Xuất hàng - Cập nhật
 * thông tin sản phẩm - Tìm kiếm sản phẩm - Xem tồn kho hiện tại - Xóa mềm sản
 * phẩm - Xem lịch sử nhập/xuất hàng
 * 
 * URL gốc: /warehouse
 */
@Controller
@RequestMapping("/warehouse")
@RequiredArgsConstructor
public class WarehouseController {

	private final ProductService productService;
	private final ImportService importService;
	private final ExportService exportService;
	private final UnitService unitService;

	/**
	 * Hiển thị danh sách sản phẩm có phân trang.
	 */
	@GetMapping
	public String listProducts(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
			Model model) {
		Page<ProductResponse> productPage = productService.findAllPaged(page, size);
		model.addAttribute("productPage", productPage);
		return "warehouse/list";
	}

	/**
	 * Hiển thị form nhập hàng.
	 */
	@GetMapping("/import")
	public String showImportForm(Model model) {
	    ImportRequest importRequest = new ImportRequest();
	    importRequest.setImportDate(LocalDate.now());
	    model.addAttribute("importRequest", importRequest);
	    model.addAttribute("units", unitService.findAll());
	    return "warehouse/import-form";
	}


	/**
	 * Xử lý khi người dùng gửi form nhập hàng.
	 */
	@PostMapping("/import")
	public String handleImport(@Valid @ModelAttribute("importRequest") ImportRequest request, BindingResult result,
			Model model, RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			model.addAttribute("units", unitService.findAll());
			return "warehouse/import-form";
		}

		try {
			importService.createImport(request);
			redirectAttributes.addFlashAttribute("success", "Nhập hàng thành công!");
			return "redirect:/warehouse";
		} catch (RuntimeException e) {
			model.addAttribute("units", unitService.findAll());
			model.addAttribute("error", "Nhập hàng thất bại: " + e.getMessage());
			return "warehouse/import-form";
		}
	}

	/**
	 * Hiển thị form xuất hàng.
	 */
	@GetMapping("/export")
	public String showExportForm(Model model) {
	    ExportRequest exportRequest = new ExportRequest();
	    exportRequest.setExportDate(LocalDate.now()); 
	    model.addAttribute("exportRequest", exportRequest);
	    model.addAttribute("products", productService.findAll());
	    return "warehouse/export-form";
	}


	/**
	 * Xử lý khi người dùng gửi form xuất hàng.
	 */
	@PostMapping("/export")
	public String handleExport(@Valid @ModelAttribute("exportRequest") ExportRequest request, BindingResult result,
			Model model, RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			model.addAttribute("products", productService.findAll());
			return "warehouse/export-form";
		}

		try {
			exportService.createExport(request);
			redirectAttributes.addFlashAttribute("success", "Xuất hàng thành công!");
			return "redirect:/warehouse";
		} catch (RuntimeException e) {
			model.addAttribute("products", productService.findAll());
			model.addAttribute("error", "Xuất hàng thất bại: " + e.getMessage());
			return "warehouse/export-form";
		}
	}

	/**
	 * Hiển thị form chỉnh sửa thông tin sản phẩm.
	 */
	@GetMapping("/edit/{id}")
	public String showEditForm(@PathVariable Integer id, Model model) {
		try {
			ProductRequest request = productService.findRequestById(id);

			// Đảm bảo unitId luôn có giá trị hiện tại
			if (request.getUnitId() == null) {
				ProductResponse entity = productService.findById(id);
				if (entity.getUnitId() != null) {
					request.setUnitId(entity.getUnitId());
				}
			}

			model.addAttribute("productRequest", request);
			model.addAttribute("productId", id);
			model.addAttribute("units", unitService.findAll());
			return "warehouse/edit-form";
		} catch (Exception e) {
			return "redirect:/warehouse";
		}
	}

	/**
	 * Cập nhật thông tin sản phẩm sau khi chỉnh sửa.
	 */
	@PostMapping("/edit/{id}")
	public String updateProduct(@PathVariable Integer id,
			@Valid @ModelAttribute("productRequest") ProductRequest request, BindingResult result, Model model,
			RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			model.addAttribute("productId", id);
			model.addAttribute("units", unitService.findAll());
			return "warehouse/edit-form";
		}

		try {
			productService.update(id, request);
			redirectAttributes.addFlashAttribute("success", "Cập nhật sản phẩm thành công!");
			return "redirect:/warehouse";
		} catch (RuntimeException e) {
			model.addAttribute("productId", id);
			model.addAttribute("units", unitService.findAll());
			model.addAttribute("error", "Cập nhật thất bại: " + e.getMessage());
			return "warehouse/edit-form";
		}
	}

	/**
	 * Tìm kiếm sản phẩm theo từ khóa.
	 */
	@GetMapping("/search")
	public String search(@RequestParam String keyword, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, Model model) {
		Page<ProductResponse> productPage = productService.search(keyword, page, size);
		model.addAttribute("productPage", productPage);
		model.addAttribute("keyword", keyword);
		return "warehouse/list";
	}

	/**
	 * Lấy số lượng tồn kho hiện tại cho một sản phẩm (trả về plain text).
	 */
	@GetMapping("/stock/{productId}")
	@ResponseBody
	public String getStock(@PathVariable Integer productId) {
		int stock = productService.getCurrentStock(productId);
		return "Tồn kho hiện tại: " + stock;
	}

	/**
	 * Xóa mềm sản phẩm theo ID.
	 */
	@GetMapping("/delete/{id}")
	public String softDelete(@PathVariable Integer id,
	                         @RequestParam(defaultValue = "0") int page,
	                         RedirectAttributes redirectAttributes) {
	    productService.delete(id);
	    redirectAttributes.addFlashAttribute("success", "Xóa sản phẩm thành công!");
	    return "redirect:/warehouse?page=" + page;
	}


	/**
	 * Hiển thị lịch sử nhập hàng.
	 */
	@GetMapping("/history/import")
	public String viewAllImportHistory(Model model) {
		model.addAttribute("imports", importService.getAll());
		return "warehouse/import-history";
	}

	/**
	 * Hiển thị lịch sử xuất hàng.
	 */
	@GetMapping("/history/export")
	public String viewAllExportHistory(Model model) {
		model.addAttribute("exports", exportService.getAll());
		return "warehouse/export-history";
	}
}
