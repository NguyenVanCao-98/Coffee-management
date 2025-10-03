package com.viettridao.cafe.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

import com.viettridao.cafe.dto.request.menu.MenuDetailRequest;
import com.viettridao.cafe.dto.request.menu.MenuItemRequest;
import com.viettridao.cafe.dto.response.menu.MenuItemResponse;
import com.viettridao.cafe.repository.ProductRepository;
import com.viettridao.cafe.repository.UnitRepository;
import com.viettridao.cafe.service.MenuItemService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/menu")
@RequiredArgsConstructor
public class MenuItemController {

	private final MenuItemService menuItemService;
	private final ProductRepository productRepository;
	private final UnitRepository unitRepository;

	@GetMapping
	public String listMenu(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
			@RequestParam(required = false) String keyword, Model model) {

		Pageable pageable = PageRequest.of(page, size, Sort.by("itemName").ascending());
		Page<MenuItemResponse> menuPage;

		if (keyword != null && !keyword.isBlank()) {
			menuPage = menuItemService.search(keyword, pageable);
			model.addAttribute("keyword", keyword);
		} else {
			menuPage = menuItemService.getAll(pageable);
		}

		model.addAttribute("menuPage", menuPage);
		return "menu/list";
	}

	@GetMapping("/add")
	public String addForm(Model model) {
		model.addAttribute("menuItemRequest", new MenuItemRequest());
		model.addAttribute("products", productRepository.findAllByIsDeletedFalse());
		model.addAttribute("units", unitRepository.findAllByIsDeletedFalse());
		return "menu/add";
	}

	@PostMapping("/add")
	public String addMenu(@Valid @ModelAttribute("menuItemRequest") MenuItemRequest menuItemRequest,
	                      BindingResult result,
	                      Model model,
	                      RedirectAttributes redirectAttributes) {

	    model.addAttribute("products", productRepository.findAllByIsDeletedFalse());
	    model.addAttribute("units", unitRepository.findAllByIsDeletedFalse());

	    if (result.hasErrors()) {
	        model.addAttribute("error", "Vui lòng kiểm tra thông tin nhập ");
	        return "menu/add"; // 
	    }

	    try {
	        menuItemService.create(menuItemRequest);
	        redirectAttributes.addFlashAttribute("success", "Món ăn đã được thêm vào danh sách thành công 🎉");
	        return "redirect:/menu"; // redirect chỉ khi thêm thành công
	    } catch (RuntimeException ex) {
	        model.addAttribute("error", "Món này đã tồn tại! ");
	        return "menu/add"; // giữ lại form, không redirect
	    }
	}


	@GetMapping("/edit/{id}")
	public String editForm(@PathVariable Integer id, Model model) {
		MenuItemResponse menuItem = menuItemService.getById(id);

		MenuItemRequest request = new MenuItemRequest();
		request.setItemName(menuItem.getItemName());
		request.setCurrentPrice(menuItem.getCurrentPrice());

		List<MenuDetailRequest> ingredients = menuItem.getIngredients().stream()
				.map(i -> new MenuDetailRequest(i.getProductId(), i.getQuantity(), i.getUnitName()))
				.collect(Collectors.toList());
		request.setIngredients(ingredients);

		model.addAttribute("menuItemRequest", request);
		model.addAttribute("menuId", id);
		model.addAttribute("products", productRepository.findAllByIsDeletedFalse());
		model.addAttribute("units", unitRepository.findAllByIsDeletedFalse());

		return "menu/edit";
	}

	@PostMapping("/edit/{id}")
	public String editMenu(@PathVariable Integer id,
			@Valid @ModelAttribute("menuItemRequest") MenuItemRequest menuItemRequest, BindingResult result,
			Model model, RedirectAttributes redirectAttributes) {

		model.addAttribute("products", productRepository.findAllByIsDeletedFalse());
		model.addAttribute("units", unitRepository.findAllByIsDeletedFalse());
		model.addAttribute("menuId", id);

		if (result.hasErrors()) {
			model.addAttribute("error", "Vui lòng kiểm tra thông tin nhập");
			return "menu/edit";
		}

		menuItemService.update(id, menuItemRequest);
		redirectAttributes.addFlashAttribute("success", "Thông tin món ăn đã được cập nhật thành công");
		return "redirect:/menu";
	}

	@PostMapping("/delete/{id}")
	public String deleteMenu(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
		menuItemService.delete(id);
		redirectAttributes.addFlashAttribute("success", "Món ăn đã được xóa khỏi danh sách ");
		return "redirect:/menu";
	}
}
