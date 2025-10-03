package com.viettridao.cafe.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.viettridao.cafe.model.ReservationEntity;
import com.viettridao.cafe.model.ReservationKey;

/**
 * Repository quản lý các thao tác truy vấn đối với bảng ReservationEntity (đặt
 * bàn).
 */
@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, ReservationKey> {

	/**
	 * Kiểm tra xem một bàn đã được đặt vào ngày và giờ cụ thể hay chưa (chưa bị xóa
	 * mềm).
	 *
	 * @param tableId ID của bàn
	 * @param date    ngày đặt bàn
	 * @param time    giờ đặt bàn
	 * @return true nếu đã tồn tại đặt bàn, ngược lại false
	 */
	boolean existsByTableIdAndReservationDateAndReservationTimeAndIsDeletedFalse(Integer tableId, LocalDate date,
			LocalTime time);

	/**
	 * Lấy bản ghi đặt bàn mới nhất của một bàn cụ thể (chưa bị xóa mềm), sắp xếp
	 * theo ngày và giờ đặt bàn giảm dần.
	 *
	 * @param tableId ID của bàn
	 * @return Optional chứa bản ghi đặt bàn mới nhất nếu có
	 */
	Optional<ReservationEntity> findTopByTable_IdAndIsDeletedFalseOrderByReservationDateDescReservationTimeDesc(
			Integer tableId);

	/**
	 * Lấy danh sách các bản ghi đặt bàn liên kết với một hóa đơn cụ thể (chưa bị
	 * xóa mềm).
	 *
	 * @param invoiceId ID của hóa đơn
	 * @return danh sách các đặt bàn tương ứng với hóa đơn
	 */
	List<ReservationEntity> findByInvoice_IdAndIsDeletedFalse(Integer invoiceId);
	
}
