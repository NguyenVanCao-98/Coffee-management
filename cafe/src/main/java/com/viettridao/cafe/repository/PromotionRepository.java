package com.viettridao.cafe.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.viettridao.cafe.model.PromotionEntity;

/**
 * Repository thao tác với bảng PromotionEntity (chương trình khuyến mãi). Cung
 * cấp các phương thức truy vấn chương trình khuyến mãi và hỗ trợ phân trang.
 */
@Repository
public interface PromotionRepository extends JpaRepository<PromotionEntity, Integer> {

	/**
	 * Lấy danh sách tất cả chương trình khuyến mãi chưa bị xóa mềm, có hỗ trợ phân
	 * trang.
	 *
	 * @param pageable thông tin phân trang
	 * @return danh sách phân trang các chương trình khuyến mãi đang hoạt động
	 */
	@Query("select p from PromotionEntity p where p.isDeleted = false")
	Page<PromotionEntity> getAllByPromotions(Pageable pageable);
}
