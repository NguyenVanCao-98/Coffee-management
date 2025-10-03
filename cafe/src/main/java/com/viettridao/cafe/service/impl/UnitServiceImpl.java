package com.viettridao.cafe.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.viettridao.cafe.dto.response.unit.UnitResponse;
import com.viettridao.cafe.mapper.UnitMapper;
import com.viettridao.cafe.model.UnitEntity;
import com.viettridao.cafe.repository.UnitRepository;
import com.viettridao.cafe.service.UnitService;

import lombok.RequiredArgsConstructor;

/**
 * Service xử lý các thao tác liên quan đến đơn vị tính (Unit). Triển khai
 * interface UnitService.
 */
@Service
@RequiredArgsConstructor
public class UnitServiceImpl implements UnitService {

	// Repository truy xuất dữ liệu đơn vị tính từ cơ sở dữ liệu
	private final UnitRepository unitRepository;

	// Mapper để chuyển đổi giữa Entity và DTO response
	private final UnitMapper unitMapper;

	/**
	 * Lấy danh sách tất cả các đơn vị tính chưa bị xóa (isDeleted = false).
	 * 
	 * @return Danh sách UnitResponse chứa thông tin các đơn vị tính
	 */
	@Override
	public List<UnitResponse> findAll() {
		// Gọi repository để lấy danh sách entity chưa bị xóa,
		// sau đó dùng mapper để chuyển đổi sang DTO trả về cho client
		return unitMapper.toDtoList(unitRepository.findByIsDeletedFalse());
	}
	@Override
	public UnitEntity findById(Integer id) {
	    return unitRepository.findByIdAndIsDeletedFalse(id)
	            .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn vị với ID: " + id));
	}

}
