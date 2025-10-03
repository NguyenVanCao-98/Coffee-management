package com.viettridao.cafe.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.viettridao.cafe.model.PositionEntity;
import com.viettridao.cafe.repository.PositionRepository;
import com.viettridao.cafe.service.PositionService;

import lombok.RequiredArgsConstructor;

/**
 * Triển khai dịch vụ xử lý chức vụ (Position). Gồm lấy danh sách chức vụ và tìm
 * theo ID.
 */
@Service
@RequiredArgsConstructor
public class PositionServiceImpl implements PositionService {

	private final PositionRepository positionRepository;

	/**
	 * Lấy chức vụ theo ID.
	 *
	 * @param id ID của chức vụ
	 * @return PositionEntity đối tượng chức vụ
	 * @throws RuntimeException nếu không tìm thấy
	 */
	@Override
	public PositionEntity getPositionById(Integer id) {
		return positionRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy chức vụ id=" + id));
	}

	/**
	 * Lấy tất cả chức vụ còn hoạt động.
	 *
	 * @return danh sách chức vụ
	 */
	@Override
	public List<PositionEntity> getPositions() {
		return positionRepository.getAllPositions();
	}
}
