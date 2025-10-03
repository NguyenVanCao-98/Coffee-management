package com.viettridao.cafe.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.viettridao.cafe.dto.response.account.AccountResponse;
import com.viettridao.cafe.model.AccountEntity;

/**
 * Repository xử lý truy vấn liên quan đến thực thể {@link AccountEntity}. Cung cấp các phương thức truy
 * vấn dữ liệu liên quan đến tài khoản từ cơ sở dữ liệu.
 */
@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Integer> {

	/**
	 * Tìm tài khoản theo tên đăng nhập.
	 *
	 * @param username tên đăng nhập
	 * @return Optional chứa tài khoản nếu tìm thấy
	 */
	Optional<AccountEntity> findByUsername(String username);

	/**
	 * Truy vấn tùy chỉnh để lấy thông tin tài khoản kèm theo thông tin nhân viên và
	 * chức vụ.
	 * 
	 * - Dùng projection {@link AccountResponse} để chỉ lấy các trường cần thiết. -
	 * Thực hiện join đến bảng nhân viên (employee) và bảng chức vụ (position).
	 *
	 * @param username tên đăng nhập
	 * @return Đối tượng AccountResponse chứa dữ liệu tổng hợp
	 */
	@Query("""
			    SELECT new com.viettridao.cafe.dto.response.account.AccountResponse(
			        tk.id, nv.fullName, nv.phoneNumber, nv.address, tk.imageUrl,
			        cv.id, cv.positionName, cv.salary, tk.username, tk.password)
			    FROM AccountEntity tk
			    LEFT JOIN tk.employee nv
			    LEFT JOIN nv.position cv
			    WHERE tk.username = :username
			""")
	AccountResponse getAccountByUsername(@Param("username") String username);
}
