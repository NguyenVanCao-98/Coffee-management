package com.viettridao.cafe.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.viettridao.cafe.common.TableStatus;
import com.viettridao.cafe.model.TableEntity;

/**
 * Repository quản lý các thao tác truy vấn đối với bảng TableEntity (bàn trong
 * quán).
 */
@Repository
public interface TableRepository extends JpaRepository<TableEntity, Integer> {

	/**
	 * Trả về danh sách tất cả các bàn chưa bị xóa mềm (isDeleted = false).
	 *
	 * @return danh sách các bàn còn hiệu lực
	 */
	List<TableEntity> findByIsDeletedFalse();

	List<TableEntity> findByIsDeletedFalseAndStatus(TableStatus status);


	/**
	 * Trả về trang dữ liệu các bàn chưa bị xóa mềm.
	 *
	 * @param pageable thông tin phân trang
	 * @return Page các bàn còn hiệu lực
	 */
	Page<TableEntity> findByIsDeletedFalse(Pageable pageable);

	/**
	 * Tìm kiếm bàn theo tên (tableName) có chứa chuỗi keyword (không phân biệt hoa
	 * thường) và chưa bị xóa mềm.
	 *
	 * @param keyword  từ khóa tìm kiếm
	 * @param pageable thông tin phân trang
	 * @return Page kết quả tìm kiếm
	 */
	Page<TableEntity> findByIsDeletedFalseAndTableNameContainingIgnoreCase(String keyword, Pageable pageable);

	/**
	 * Tìm bàn thông qua ID của hóa đơn có liên kết đặt bàn (Reservation → Invoice).
	 *
	 * @param invoiceId ID của hóa đơn
	 * @return TableEntity tương ứng với hóa đơn đó
	 */
	TableEntity findByReservations_Invoice_Id(Integer invoiceId);

}
