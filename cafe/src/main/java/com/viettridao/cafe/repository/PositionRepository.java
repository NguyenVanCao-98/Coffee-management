package com.viettridao.cafe.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.viettridao.cafe.model.PositionEntity;

/**
 * Repository thao tác với bảng PositionEntity (Chức vụ trong hệ thống). Cung
 * cấp các phương thức truy vấn liên quan đến chức vụ.
 */
@Repository
public interface PositionRepository extends JpaRepository<PositionEntity, Integer> {

	/**
	 * Truy vấn tất cả các chức vụ chưa bị xóa mềm (isDeleted = false).
	 *
	 * @return danh sách chức vụ đang hoạt động
	 */
	@Query("select p from PositionEntity p where p.isDeleted = false")
	List<PositionEntity> getAllPositions();
}
