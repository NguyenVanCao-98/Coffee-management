package com.viettridao.cafe.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.viettridao.cafe.dto.request.menu.MenuItemRequest;
import com.viettridao.cafe.dto.response.menu.MenuItemResponse;

public interface MenuItemService {

	void create(MenuItemRequest request);

	void update(Integer id, MenuItemRequest request);

	MenuItemResponse getById(Integer id);

	void delete(Integer id);

	Page<MenuItemResponse> getAll(Pageable pageable);

	Page<MenuItemResponse> search(String keyword, Pageable pageable);
}
