package com.viettridao.cafe.service;

/**
 * Interface định nghĩa các phương thức liên quan đến xác thực người dùng.
 */
public interface AuthService {

	/**
	 * Thực hiện đăng nhập bằng username và password.
	 *
	 * @param username tên đăng nhập của người dùng
	 * @param password mật khẩu tương ứng với username
	 * @return true nếu đăng nhập thành công, false nếu thất bại
	 */
	boolean login(String username, String password);
	
}
