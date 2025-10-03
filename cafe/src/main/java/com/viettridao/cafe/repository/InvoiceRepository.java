package com.viettridao.cafe.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.viettridao.cafe.common.InvoiceStatus;
import com.viettridao.cafe.model.InvoiceEntity;

/**
 * Repository thực hiện các thao tác truy vấn với bảng InvoiceEntity (Hóa đơn).
 */
@Repository
public interface InvoiceRepository extends JpaRepository<InvoiceEntity, Integer> {

	/**
	 * Lấy danh sách hóa đơn theo trạng thái và khoảng thời gian tạo.
	 *
	 * @param status trạng thái hóa đơn (PENDING, PAID, CANCELLED,...)
	 * @param from   thời gian bắt đầu
	 * @param to     thời gian kết thúc
	 * @return danh sách hóa đơn phù hợp
	 */
	List<InvoiceEntity> findByStatusAndCreatedAtBetween(InvoiceStatus status, LocalDateTime from, LocalDateTime to);

	/**
	 * Lấy hóa đơn mới nhất (gần nhất) của một bàn theo trạng thái và chưa bị xóa.
	 *
	 * @param tableId ID bàn
	 * @param status  trạng thái hóa đơn
	 * @return hóa đơn gần nhất theo trạng thái
	 */
	InvoiceEntity findTopByReservations_Table_IdAndStatusAndIsDeletedFalseOrderByCreatedAtDesc(Integer tableId,
			InvoiceStatus status);

	/**
	 * Tính tổng tiền của tất cả hóa đơn được thanh toán trong một ngày cụ thể.
	 *
	 * @param date ngày cần tính
	 * @return tổng doanh thu trong ngày
	 */
	@Query("SELECT SUM(i.totalAmount) FROM InvoiceEntity i WHERE DATE(i.createdAt) = :date AND i.status = 'PAID' AND i.isDeleted = false")
	Double sumTotalAmountByDate(@Param("date") LocalDate date);

	/**
	 * Tính tổng doanh thu trong khoảng thời gian chỉ tính hóa đơn đã thanh toán.
	 *
	 * @param from thời gian bắt đầu
	 * @param to   thời gian kết thúc
	 * @return tổng doanh thu
	 */
	@Query("SELECT SUM(i.totalAmount) FROM InvoiceEntity i WHERE i.createdAt BETWEEN :from AND :to AND i.status = 'PAID' AND i.isDeleted = false")
	Double sumTotalAmountBetween(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

	/**
	 * Lấy hóa đơn mới nhất (dù trạng thái nào) theo bàn nếu chưa bị xóa.
	 *
	 * @param tableId ID bàn
	 * @return hóa đơn mới nhất của bàn
	 */
	Optional<InvoiceEntity> findTopByReservations_Table_IdAndIsDeletedFalseOrderByCreatedAtDesc(Integer tableId);

}
