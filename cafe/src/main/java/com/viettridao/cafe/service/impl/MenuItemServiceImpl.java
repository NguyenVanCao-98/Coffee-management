package com.viettridao.cafe.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.viettridao.cafe.dto.request.menu.MenuItemRequest;
import com.viettridao.cafe.dto.response.menu.MenuItemResponse;
import com.viettridao.cafe.mapper.MenuItemMapper;
import com.viettridao.cafe.model.MenuDetailEntity;
import com.viettridao.cafe.model.MenuItemEntity;
import com.viettridao.cafe.model.ProductEntity;
import com.viettridao.cafe.repository.MenuDetailRepository;
import com.viettridao.cafe.repository.MenuItemRepository;
import com.viettridao.cafe.repository.ProductRepository;
import com.viettridao.cafe.service.MenuItemService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MenuItemServiceImpl implements MenuItemService {

	private final MenuItemRepository menuItemRepository;
	private final MenuDetailRepository menuDetailRepository;
	private final ProductRepository productRepository;
	private final MenuItemMapper menuItemMapper;

	@Override
	public void create(MenuItemRequest request) {
		if (menuItemRepository.existsByItemNameIgnoreCaseAndIsDeletedFalse(request.getItemName())) {
			throw new RuntimeException("Món này đã tồn tại!");
		}

		MenuItemEntity menuItem = new MenuItemEntity();
		menuItem.setItemName(request.getItemName());
		menuItem.setCurrentPrice(request.getCurrentPrice());
		menuItem.setIsDeleted(false);
		menuItemRepository.save(menuItem);

		List<MenuDetailEntity> details = request.getIngredients().stream().map(d -> {
			ProductEntity product = productRepository.findById(d.getProductId())
					.orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

			MenuDetailEntity detail = new MenuDetailEntity();
			detail.setMenuItem(menuItem);
			detail.setProduct(product);
			detail.setQuantity(d.getQuantity());
			detail.setUnitName(d.getUnitName());
			detail.setIsDeleted(false);

			return detail;
		}).toList();

		menuDetailRepository.saveAll(details);
	}

	@Override
	public void update(Integer id, MenuItemRequest request) {
		MenuItemEntity menuItem = menuItemRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy món"));

		menuItem.setItemName(request.getItemName());
		menuItem.setCurrentPrice(request.getCurrentPrice());
		menuItemRepository.save(menuItem);

		// Xóa chi tiết cũ
		menuDetailRepository.deleteByMenuItemId(id);

		List<MenuDetailEntity> details = request.getIngredients().stream().map(d -> {
			ProductEntity product = productRepository.findById(d.getProductId())
					.orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

			MenuDetailEntity detail = new MenuDetailEntity();
			detail.setMenuItem(menuItem);
			detail.setProduct(product);
			detail.setQuantity(d.getQuantity());
			detail.setUnitName(d.getUnitName());
			detail.setIsDeleted(false);

			return detail;
		}).toList();

		menuDetailRepository.saveAll(details);
	}

	@Override
	public MenuItemResponse getById(Integer id) {
		MenuItemEntity menuItem = menuItemRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy món"));
		return menuItemMapper.toResponse(menuItem);
	}

	@Override
	public void delete(Integer id) {
		MenuItemEntity menuItem = menuItemRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy món"));
		menuItem.setIsDeleted(true);
		menuItemRepository.save(menuItem);
	}

	@Override
	public Page<MenuItemResponse> getAll(Pageable pageable) {
		return menuItemRepository.findByIsDeletedFalse(pageable).map(menuItemMapper::toResponse);
	}

	@Override
	public Page<MenuItemResponse> search(String keyword, Pageable pageable) {
		return menuItemRepository.findByIsDeletedFalseAndItemNameContainingIgnoreCase(keyword, pageable)
				.map(menuItemMapper::toResponse);
	}

}
