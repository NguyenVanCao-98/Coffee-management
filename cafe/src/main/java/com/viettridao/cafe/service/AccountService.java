package com.viettridao.cafe.service;

import com.viettridao.cafe.dto.request.account.UpdateAccountRequest;
import com.viettridao.cafe.model.AccountEntity;

/**
 * Interface định nghĩa các phương thức liên quan đến quản lý tài khoản người
 * dùng.
 */
public interface AccountService {

	/**
	 * Cập nhật thông tin tài khoản dựa trên dữ liệu từ request.
	 * 
	 * @param request đối tượng chứa dữ liệu cần cập nhật cho tài khoản
	 */
	void updateAccount(UpdateAccountRequest request);

	/**
	 * Lấy thông tin tài khoản theo ID tài khoản.
	 * 
	 * @param id ID của tài khoản cần truy vấn
	 * @return AccountEntity đối tượng tài khoản tương ứng với ID
	 */
	AccountEntity getAccountById(Integer id);

	/**
	 * Lấy thông tin tài khoản theo tên đăng nhập (username).
	 * 
	 * @param username tên đăng nhập của tài khoản cần truy vấn
	 * @return AccountEntity đối tượng tài khoản tương ứng với username
	 */
	AccountEntity getAccountByUsername(String username);
}
