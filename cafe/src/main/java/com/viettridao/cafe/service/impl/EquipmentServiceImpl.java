package com.viettridao.cafe.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.viettridao.cafe.dto.request.equipment.CreateEquipmentRequest;
import com.viettridao.cafe.dto.request.equipment.UpdateEquipmentRequest;
import com.viettridao.cafe.dto.response.equipment.EquipmentPageResponse;
import com.viettridao.cafe.mapper.EquipmentMapper;
import com.viettridao.cafe.model.EquipmentEntity;
import com.viettridao.cafe.repository.EquipmentRepository;
import com.viettridao.cafe.service.EquipmentService;

import lombok.RequiredArgsConstructor;

/**
 * Service triển khai các chức năng quản lý thiết bị (equipment), bao gồm tạo,
 * cập nhật, xóa mềm, phân trang và lấy danh sách thiết bị.
 */
@Service
@RequiredArgsConstructor
public class EquipmentServiceImpl implements EquipmentService {

	private final EquipmentRepository equipmentRepository;

	private final EquipmentMapper equipmentMapper;

	/**
	 * Lấy danh sách tất cả thiết bị chưa bị xóa (isDeleted = false)
	 *
	 * @return List<EquipmentEntity> - danh sách thiết bị
	 */
	@Override
	public List<EquipmentEntity> getAllEquipments() {
		return equipmentRepository.getAllEquipments();
	}

	/**
	 * Tạo mới một thiết bị và lưu vào cơ sở dữ liệu
	 *
	 * @param request CreateEquipmentRequest - thông tin thiết bị cần tạo
	 * @return EquipmentEntity - thiết bị đã được lưu
	 */
	@Transactional
	@Override
	public EquipmentEntity createEquipment(CreateEquipmentRequest request) {
		// Khởi tạo thực thể mới và gán thông tin từ request
		EquipmentEntity equipmentEntity = new EquipmentEntity();
		equipmentEntity.setEquipmentName(request.getEquipmentName());
		equipmentEntity.setQuantity(request.getQuantity());
		equipmentEntity.setPurchaseDate(request.getPurchaseDate());
		equipmentEntity.setPurchasePrice(request.getPurchasePrice());
		equipmentEntity.setIsDeleted(false); // Thiết bị mặc định chưa bị xóa

		// Lưu vào database và trả về đối tượng đã lưu
		return equipmentRepository.save(equipmentEntity);
	}

	/**
	 * Đánh dấu xóa mềm cho thiết bị (chỉ set isDeleted = true)
	 *
	 * @param id Integer - ID thiết bị cần xóa
	 */
	@Transactional
	@Override
	public void deleteEquipment(Integer id) {
		// Lấy thiết bị cần xóa, nếu không tồn tại sẽ ném exception
		EquipmentEntity equipment = getEquipmentById(id);
		equipment.setIsDeleted(true); // Đánh dấu xóa

		// Lưu lại trạng thái mới
		equipmentRepository.save(equipment);
	}

	/**
	 * Lấy thiết bị theo ID
	 *
	 * @param id Integer - ID thiết bị
	 * @return EquipmentEntity - thiết bị tương ứng
	 * @throws RuntimeException nếu không tìm thấy thiết bị
	 */
	@Override
	public EquipmentEntity getEquipmentById(Integer id) {
		return equipmentRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy thiết bị có id=" + id));
	}

	/**
	 * Cập nhật thông tin thiết bị dựa trên ID và dữ liệu mới
	 *
	 * @param request UpdateEquipmentRequest - dữ liệu thiết bị cần cập nhật
	 */
	@Transactional
	@Override
	public void updateEquipment(UpdateEquipmentRequest request) {
		// Lấy thiết bị cần cập nhật
		EquipmentEntity equipmentEntity = getEquipmentById(request.getId());

		// Gán các thông tin mới từ request
		equipmentEntity.setEquipmentName(request.getEquipmentName());
		equipmentEntity.setQuantity(request.getQuantity());
		equipmentEntity.setPurchaseDate(request.getPurchaseDate());
		equipmentEntity.setPurchasePrice(request.getPurchasePrice());

		// Lưu thiết bị sau khi cập nhật
		equipmentRepository.save(equipmentEntity);
	}

	/**
	 * Lấy danh sách thiết bị có phân trang
	 *
	 * @param page int - số trang cần lấy (bắt đầu từ 0)
	 * @param size int - số lượng phần tử trên mỗi trang
	 * @return EquipmentPageResponse - chứa thông tin trang và danh sách thiết bị
	 *         dạng DTO
	 */
	@Override
	public EquipmentPageResponse getAllEquipmentsPage(int page, int size) {
		// Tạo đối tượng phân trang
		Pageable pageable = PageRequest.of(page, size);

		// Lấy danh sách thiết bị đã phân trang (chỉ lấy isDeleted = false)
		Page<EquipmentEntity> equipmentEntities = equipmentRepository.getAllEquipmentsByPage(pageable);

		// Tạo response phân trang và gán dữ liệu
		EquipmentPageResponse equipmentPageResponse = new EquipmentPageResponse();
		equipmentPageResponse.setPageNumber(equipmentEntities.getNumber());
		equipmentPageResponse.setTotalElements(equipmentEntities.getTotalElements());
		equipmentPageResponse.setTotalPages(equipmentEntities.getTotalPages());
		equipmentPageResponse.setPageSize(equipmentEntities.getSize());

		// Dùng mapper để chuyển list entity sang list DTO
		equipmentPageResponse.setEquipments(equipmentMapper.toDtoList(equipmentEntities.getContent()));

		return equipmentPageResponse;
	}
}
