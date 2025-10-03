package com.viettridao.cafe.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.viettridao.cafe.repository.AccountRepository;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Lớp dịch vụ triển khai interface {@link UserDetailsService} của Spring
 * Security. Chịu trách nhiệm tải thông tin chi tiết người dùng (UserDetails)
 * dựa trên tên người dùng (username) để phục vụ quá trình xác thực.
 */
@Service
@RequiredArgsConstructor
@Getter
public class UserServiceDetail implements UserDetailsService {

	/**
	 * Repository để truy cập dữ liệu tài khoản từ cơ sở dữ liệu.
	 */
	private final AccountRepository accountRepository;

	/**
	 * Tải thông tin chi tiết người dùng dựa trên tên người dùng. Phương thức này
	 * được Spring Security gọi trong quá trình xác thực.
	 *
	 * @param username Tên người dùng cần tải.
	 * @return Đối tượng {@link UserDetails} chứa thông tin chi tiết của người dùng.
	 * @throws UsernameNotFoundException Nếu không tìm thấy tài khoản với tên người
	 *                                   dùng được cung cấp.
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return accountRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy tài khoản có username = " + username));
	}
}