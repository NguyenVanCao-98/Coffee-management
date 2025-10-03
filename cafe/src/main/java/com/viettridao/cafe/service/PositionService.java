package com.viettridao.cafe.service;

import java.util.List;

import com.viettridao.cafe.model.PositionEntity;

/**
 * Interface định nghĩa các phương thức liên quan đến chức vụ (Position) trong
 * hệ thống.
 */
public interface PositionService {

	/**
	 * Lấy thông tin chức vụ theo ID.
	 *
	 * @param id ID của chức vụ cần lấy thông tin
	 * @return đối tượng PositionEntity tương ứng với ID truyền vào, nếu không tìm
	 *         thấy có thể trả về null hoặc ném ngoại lệ
	 */
	PositionEntity getPositionById(Integer id);

	/**
	 * Lấy danh sách tất cả các chức vụ hiện có trong hệ thống.
	 *
	 * @return danh sách các đối tượng PositionEntity
	 */
	List<PositionEntity> getPositions();
}
