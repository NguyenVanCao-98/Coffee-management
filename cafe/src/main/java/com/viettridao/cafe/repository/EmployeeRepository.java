package com.viettridao.cafe.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.viettridao.cafe.model.EmployeeEntity;

/**
 * Repository xử lý truy vấn liên quan đến thực thể {@link EmployeeEntity}. Cung
 * cấp các phương thức truy xuất thông tin nhân viên từ cơ sở dữ liệu.
 */
@Repository // Đánh dấu lớp là tầng truy xuất dữ liệu
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Integer> {

	/**
	 * Tìm kiếm nhân viên theo từ khóa tên, không phân biệt chữ hoa thường, và không
	 * bị xóa mềm.
	 *
	 * @param keyword  từ khóa tìm kiếm theo tên
	 * @param pageable đối tượng phân trang
	 * @return danh sách nhân viên phù hợp với từ khóa
	 */
	@Query("select e from EmployeeEntity e where e.isDeleted = false and lower(e.fullName) like lower(CONCAT('%', :keyword, '%')) ")
	Page<EmployeeEntity> getAllEmployeesBySearch(@Param("keyword") String keyword, Pageable pageable);

	/**
	 * Lấy tất cả nhân viên chưa bị xóa mềm, có hỗ trợ phân trang.
	 *
	 * @param pageable đối tượng phân trang
	 * @return danh sách nhân viên chưa bị xóa
	 */
	@Query("select e from EmployeeEntity e where e.isDeleted = false")
	Page<EmployeeEntity> getAllEmployees(Pageable pageable);

	/**
	 * Lấy danh sách tất cả nhân viên chưa bị xóa mềm.
	 *
	 * @return danh sách nhân viên
	 */
	List<EmployeeEntity> findByIsDeletedFalse();

	/**
	 * Tìm nhân viên theo tên tài khoản liên kết, nếu chưa bị xóa mềm.
	 *
	 * @param username tên đăng nhập tài khoản
	 * @return Optional chứa nhân viên nếu tìm thấy
	 */
	@Query("""
			    SELECT e FROM EmployeeEntity e
			    WHERE e.account.username = :username AND e.isDeleted = false
			""")
	Optional<EmployeeEntity> findByAccountUsername(@Param("username") String username);

	/**
	 * Tính tổng lương của tất cả nhân viên đang làm việc (chưa bị xóa và có chức
	 * vụ).
	 *
	 * @return tổng lương dưới dạng Double
	 */
	@Query("SELECT SUM(e.position.salary) FROM EmployeeEntity e WHERE e.isDeleted = false AND e.position IS NOT NULL")
	Double sumAllSalaries();

	/**
	 * Tìm nhân viên theo ID nếu chưa bị xóa mềm.
	 *
	 * @param id ID nhân viên
	 * @return Optional chứa nhân viên nếu tìm thấy
	 */
	Optional<EmployeeEntity> findByIdAndIsDeletedFalse(Integer id);
}
