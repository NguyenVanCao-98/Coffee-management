package com.viettridao.cafe.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.viettridao.cafe.model.ExpenseEntity;

/**
 * Repository xử lý các thao tác truy vấn liên quan đến chi phí (ExpenseEntity).
 */
@Repository
public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Integer> {

	/**
	 * Truy vấn tất cả các khoản chi phát sinh trong khoảng thời gian được chỉ định.
	 * Bỏ qua các bản ghi đã bị xóa mềm.
	 *
	 * @param from ngày bắt đầu
	 * @param to   ngày kết thúc
	 * @return danh sách khoản chi trong khoảng thời gian
	 */
	@Query("SELECT e FROM ExpenseEntity e WHERE e.expenseDate BETWEEN :from AND :to AND e.isDeleted = false")
	List<ExpenseEntity> findExpensesBetweenDates(@Param("from") LocalDate from, @Param("to") LocalDate to);

	/**
	 * Tính tổng số tiền đã chi trong một ngày cụ thể. Bỏ qua các bản ghi đã bị xóa
	 * mềm.
	 *
	 * @param date ngày muốn tính tổng chi
	 * @return tổng số tiền chi trong ngày
	 */
	@Query("SELECT SUM(e.amount) FROM ExpenseEntity e WHERE e.expenseDate = :date AND e.isDeleted = false")
	Double sumAmountByDate(@Param("date") LocalDate date);

	/**
	 * Tính tổng số tiền đã chi trong khoảng thời gian cụ thể. Bỏ qua các bản ghi đã
	 * bị xóa mềm.
	 *
	 * @param from ngày bắt đầu
	 * @param to   ngày kết thúc
	 * @return tổng chi phí trong khoảng thời gian
	 */
	@Query("SELECT SUM(e.amount) FROM ExpenseEntity e WHERE e.expenseDate BETWEEN :from AND :to AND e.isDeleted = false")
	Double sumAmountBetween(@Param("from") LocalDate from, @Param("to") LocalDate to);
}
