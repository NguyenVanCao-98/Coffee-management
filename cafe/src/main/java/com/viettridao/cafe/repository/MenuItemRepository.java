package com.viettridao.cafe.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.viettridao.cafe.model.MenuItemEntity;

/**
 * Repository thao tác với bảng MenuItemEntity (món ăn trong thực đơn). Cung cấp
 * các phương thức truy vấn tùy chỉnh liên quan đến món ăn.
 */
@Repository
public interface MenuItemRepository extends JpaRepository<MenuItemEntity, Integer> {

	/**
	 * Tìm danh sách món ăn theo từ khóa (không phân biệt hoa/thường), phân trang,
	 * chỉ lấy món chưa bị xóa mềm.
	 */
	Page<MenuItemEntity> findByIsDeletedFalseAndItemNameContainingIgnoreCase(String keyword, Pageable pageable);

	/**
	 * Kiểm tra sự tồn tại của món theo tên (không phân biệt hoa/thường) và chưa bị
	 * xóa mềm.
	 */
	boolean existsByItemNameIgnoreCaseAndIsDeletedFalse(String itemName);

	/**
	 * Lấy toàn bộ món chưa bị xóa mềm.
	 */
	List<MenuItemEntity> findByIsDeletedFalse();

	/**
	 * Tìm món ăn theo ID và chưa bị xóa mềm.
	 */
	Optional<MenuItemEntity> findByIdAndIsDeletedFalse(Integer id);

	/**
	 * Tìm món ăn theo tên (không phân biệt hoa/thường) và chưa bị xóa mềm.
	 */
	Optional<MenuItemEntity> findByItemNameIgnoreCaseAndIsDeletedFalse(String itemName);
	
	 // Lấy tất cả món chưa xóa, phân trang
    Page<MenuItemEntity> findByIsDeletedFalse(Pageable pageable);

    // Kiểm tra trùng tên
    boolean existsByItemNameAndIsDeletedFalse(String itemName);
    
}
