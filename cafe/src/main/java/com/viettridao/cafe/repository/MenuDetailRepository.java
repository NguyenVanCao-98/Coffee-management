package com.viettridao.cafe.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.viettridao.cafe.model.MenuDetailEntity;
import com.viettridao.cafe.model.MenuKey;

/**
 * Repository thao tác với bảng MenuDetailEntity (chi tiết món trong thực đơn).
 * Sử dụng khóa chính tổng hợp {@link MenuKey}.
 */
@Repository
public interface MenuDetailRepository extends JpaRepository<MenuDetailEntity, Long> {

	List<MenuDetailEntity> findByMenuItem_IdAndIsDeletedFalse(Integer menuItemId);

	@Modifying
	@Query("DELETE FROM MenuDetailEntity m WHERE m.menuItem.id = :menuItemId")
	void deleteByMenuItemId(@Param("menuItemId") Integer menuItemId);

	@Query("SELECT DISTINCT m.unitName FROM MenuDetailEntity m WHERE m.isDeleted = false")
	List<String> findAllDistinctUnits();

	/**
	 * Lấy toàn bộ chi tiết của 1 món (chưa xóa mềm).
	 */
	default List<MenuDetailEntity> findActiveByMenuItemId(Integer menuItemId) {
		return findByMenuItem_IdAndIsDeletedFalse(menuItemId);
	}

}
