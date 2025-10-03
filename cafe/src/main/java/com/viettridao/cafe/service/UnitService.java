package com.viettridao.cafe.service;

import java.util.List;

import com.viettridao.cafe.dto.response.unit.UnitResponse;
import com.viettridao.cafe.model.UnitEntity;

/**
 * Giao diện dịch vụ cho việc quản lý các đơn vị (ví dụ: đơn vị tính của sản
 * phẩm).
 */
public interface UnitService {

	/**
	 * Lấy tất cả các đơn vị hiện có.
	 *
	 * @return Danh sách các đối tượng UnitResponse đại diện cho các đơn vị.
	 */
	List<UnitResponse> findAll();
	
    UnitEntity findById(Integer id);
	
}