package com.viettridao.cafe.service;

import com.viettridao.cafe.dto.request.employee.CreateEmployeeRequest;
import com.viettridao.cafe.dto.request.employee.UpdateEmployeeRequest;
import com.viettridao.cafe.dto.response.employee.EmployeePageResponse;
import com.viettridao.cafe.model.EmployeeEntity;

/**
 * Interface định nghĩa các phương thức phục vụ quản lý nhân viên trong hệ
 * thống.
 */
public interface EmployeeService {

	/**
	 * Lấy danh sách nhân viên theo trang với tùy chọn tìm kiếm theo từ khóa.
	 *
	 * @param keyword từ khóa tìm kiếm (có thể là tên, mã nhân viên,...)
	 * @param page    số trang muốn lấy
	 * @param size    số lượng nhân viên trên mỗi trang
	 * @return đối tượng EmployeePageResponse chứa danh sách nhân viên và thông tin
	 *         phân trang
	 */
	EmployeePageResponse getAllEmployees(String keyword, int page, int size);

	/**
	 * Tạo mới một nhân viên dựa trên thông tin trong request.
	 *
	 * @param request dữ liệu dùng để tạo nhân viên mới
	 * @return đối tượng EmployeeEntity vừa được tạo và lưu vào database
	 */
	EmployeeEntity createEmployee(CreateEmployeeRequest request);

	/**
	 * Xóa nhân viên dựa trên ID.
	 *
	 * @param id ID của nhân viên cần xóa
	 */
	void deleteEmployee(Integer id);

	/**
	 * Cập nhật thông tin nhân viên dựa trên dữ liệu truyền vào.
	 *
	 * @param request dữ liệu mới cập nhật cho nhân viên
	 */
	void updateEmployee(UpdateEmployeeRequest request);

	/**
	 * Lấy thông tin chi tiết của nhân viên theo ID.
	 *
	 * @param id ID của nhân viên cần lấy thông tin
	 * @return đối tượng EmployeeEntity tương ứng với ID hoặc null nếu không tìm
	 *         thấy
	 */
	EmployeeEntity getEmployeeById(Integer id);
}
