package com.viettridao.cafe.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.viettridao.cafe.model.UnitEntity;

/**
 * Repository xử lý các thao tác truy vấn liên quan đến bảng UnitEntity (đơn vị
 * tính).
 */
@Repository
public interface UnitRepository extends JpaRepository<UnitEntity, Integer> {

	/**
	 * Trả về danh sách tất cả đơn vị chưa bị xóa mềm (isDeleted = false).
	 *
	 * @return danh sách đơn vị còn hiệu lực
	 */
	List<UnitEntity> findByIsDeletedFalse();

	/**
	 * Tìm đơn vị theo tên đơn vị (không phân biệt hoa thường) và chưa bị xóa mềm.
	 *
	 * @param unitName tên đơn vị cần tìm
	 * @return Optional đơn vị tương ứng nếu tồn tại
	 */
	Optional<UnitEntity> findByUnitNameIgnoreCaseAndIsDeletedFalse(String unitName);

	/**
	 * Trả về toàn bộ danh sách đơn vị chưa bị xóa mềm. (Giống với
	 * findByIsDeletedFalse, có thể gộp nếu muốn.)
	 *
	 * @return danh sách đơn vị còn hiệu lực
	 */
	List<UnitEntity> findAllByIsDeletedFalse();

	/**
	 * Tìm đơn vị theo ID và chưa bị xóa mềm.
	 *
	 * @param id mã đơn vị
	 * @return Optional đơn vị tương ứng nếu tồn tại
	 */
	Optional<UnitEntity> findByIdAndIsDeletedFalse(Integer id);
	
	Optional<UnitEntity> findByUnitNameIgnoreCase(String unitName);


}
