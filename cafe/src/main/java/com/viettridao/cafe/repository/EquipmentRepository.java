package com.viettridao.cafe.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.viettridao.cafe.model.EquipmentEntity;

/**
 * Repository xử lý các thao tác liên quan đến thực thể {@link EquipmentEntity}.
 * Cung cấp các phương thức truy vấn thiết bị từ cơ sở dữ liệu.
 */
@Repository
public interface EquipmentRepository extends JpaRepository<EquipmentEntity, Integer> {

	/**
	 * Lấy danh sách tất cả thiết bị chưa bị xóa mềm.
	 *
	 * @return danh sách thiết bị còn tồn tại trong hệ thống
	 */
	@Query("select e from EquipmentEntity e where e.isDeleted = false")
	List<EquipmentEntity> getAllEquipments();

	/**
	 * Lấy danh sách tất cả thiết bị chưa bị xóa mềm có phân trang.
	 *
	 * @param pageable thông tin phân trang
	 * @return trang chứa danh sách thiết bị
	 */
	@Query("select e from EquipmentEntity e where e.isDeleted = false")
	Page<EquipmentEntity> getAllEquipmentsByPage(Pageable pageable);

	/**
	 * Tìm tất cả thiết bị được mua trong khoảng thời gian chỉ định và chưa bị xóa
	 * mềm.
	 *
	 * @param from ngày bắt đầu
	 * @param to   ngày kết thúc
	 * @return danh sách thiết bị được mua trong khoảng thời gian
	 */
	@Query("SELECT e FROM EquipmentEntity e WHERE e.purchaseDate BETWEEN :from AND :to AND e.isDeleted = false")
	List<EquipmentEntity> findEquipmentsBetweenDates(@Param("from") LocalDate from, @Param("to") LocalDate to);
}
