package com.viettridao.cafe.service;

import org.springframework.data.domain.Page;

import com.viettridao.cafe.dto.request.expenses.BudgetFilterRequest;
import com.viettridao.cafe.dto.request.expenses.ExpenseRequest;
import com.viettridao.cafe.dto.response.expenses.BudgetViewResponse;

/**
 * Interface định nghĩa các phương thức liên quan đến quản lý ngân sách và chi
 * phí.
 */
public interface BudgetService {

	/**
	 * Lấy danh sách ngân sách dưới dạng phân trang dựa trên các tiêu chí lọc.
	 *
	 * @param request các tham số lọc ngân sách như thời gian, loại chi phí,...
	 * @return trang chứa danh sách BudgetViewResponse phù hợp với điều kiện lọc
	 */
	Page<BudgetViewResponse> getBudgetView(BudgetFilterRequest request);

	/**
	 * Thêm một khoản chi mới vào hệ thống ngân sách.
	 *
	 * @param request  thông tin chi phí cần thêm
	 * @param username tên người dùng thực hiện thao tác (để ghi nhận hoặc kiểm tra
	 *                 quyền)
	 */
	void addExpense(ExpenseRequest request, String username);
}
