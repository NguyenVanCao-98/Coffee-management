package com.viettridao.cafe.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.viettridao.cafe.model.ProductEntity;

/**
 * Repository thao tác với bảng ProductEntity (Sản phẩm - nguyên liệu). Cung cấp
 * các phương thức truy vấn sản phẩm theo tên, ID, trạng thái xóa, và hỗ trợ
 * phân trang.
 */
@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Integer> {

	/**
	 * Lấy danh sách tất cả sản phẩm chưa bị xóa mềm.
	 *
	 * @return danh sách sản phẩm đang hoạt động
	 */
	List<ProductEntity> findAllByIsDeletedFalse();

	/**
	 * Lấy danh sách sản phẩm chưa bị xóa mềm, có hỗ trợ phân trang.
	 *
	 * @param pageable thông tin phân trang
	 * @return trang sản phẩm hợp lệ
	 */
	Page<ProductEntity> findAllByIsDeletedFalse(Pageable pageable);

	/**
	 * Tìm sản phẩm theo tên (không phân biệt hoa thường).
	 *
	 * @param productName tên sản phẩm
	 * @return sản phẩm tương ứng (nếu có)
	 */
	Optional<ProductEntity> findByProductNameIgnoreCase(String productName);

	/**
	 * Tìm sản phẩm theo từ khóa tìm kiếm trong tên, không phân biệt hoa thường, và
	 * chưa bị xóa mềm.
	 *
	 * @param keyword từ khóa tìm kiếm
	 * @return danh sách sản phẩm phù hợp
	 */
	List<ProductEntity> findByProductNameContainingIgnoreCaseAndIsDeletedFalse(String keyword);

	/**
	 * Tìm sản phẩm theo từ khóa tên (không phân biệt hoa thường), có hỗ trợ phân
	 * trang và lọc isDeleted = false.
	 *
	 * @param keyword  từ khóa tìm kiếm
	 * @param pageable thông tin phân trang
	 * @return trang sản phẩm phù hợp
	 */
	Page<ProductEntity> findByProductNameContainingIgnoreCaseAndIsDeletedFalse(String keyword, Pageable pageable);

	/**
	 * Tìm sản phẩm theo ID nếu sản phẩm chưa bị xóa mềm.
	 *
	 * @param id ID sản phẩm
	 * @return sản phẩm tương ứng (nếu có)
	 */
	Optional<ProductEntity> findByIdAndIsDeletedFalse(Integer id);
	
	
}
