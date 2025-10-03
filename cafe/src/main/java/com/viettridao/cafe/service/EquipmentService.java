package com.viettridao.cafe.service;

import java.util.List;

import com.viettridao.cafe.dto.request.equipment.CreateEquipmentRequest;
import com.viettridao.cafe.dto.request.equipment.UpdateEquipmentRequest;
import com.viettridao.cafe.dto.response.equipment.EquipmentPageResponse;
import com.viettridao.cafe.model.EquipmentEntity;

/**
 * Interface định nghĩa các phương thức phục vụ quản lý thiết bị trong hệ thống.
 */
public interface EquipmentService {

	/**
	 * Lấy danh sách tất cả các thiết bị chưa bị xóa.
	 *
	 * @return danh sách EquipmentEntity
	 */
	List<EquipmentEntity> getAllEquipments();

	/**
	 * Tạo mới một thiết bị dựa trên thông tin trong request.
	 *
	 * @param request dữ liệu dùng để tạo thiết bị mới
	 * @return đối tượng EquipmentEntity vừa được tạo và lưu vào database
	 */
	EquipmentEntity createEquipment(CreateEquipmentRequest request);

	/**
	 * Xóa thiết bị dựa trên ID.
	 *
	 * @param id ID của thiết bị cần xóa
	 */
	void deleteEquipment(Integer id);

	/**
	 * Lấy thông tin chi tiết của thiết bị theo ID.
	 *
	 * @param id ID của thiết bị cần lấy thông tin
	 * @return đối tượng EquipmentEntity tương ứng với ID hoặc null nếu không tìm
	 *         thấy
	 */
	EquipmentEntity getEquipmentById(Integer id);

	/**
	 * Cập nhật thông tin thiết bị dựa trên dữ liệu truyền vào.
	 *
	 * @param request dữ liệu mới cập nhật cho thiết bị
	 */
	void updateEquipment(UpdateEquipmentRequest request);

	/**
	 * Lấy danh sách thiết bị theo trang với phân trang.
	 *
	 * @param page số trang muốn lấy
	 * @param size số lượng thiết bị trên mỗi trang
	 * @return đối tượng EquipmentPageResponse chứa danh sách thiết bị và thông tin
	 *         phân trang
	 */
	EquipmentPageResponse getAllEquipmentsPage(int page, int size);
}
