package com.viettridao.cafe.service.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.viettridao.cafe.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

/**
 * Triển khai service xử lý đăng nhập người dùng. Sử dụng AuthenticationManager
 * để xác thực người dùng và lưu trữ context bảo mật vào session.
 */
@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

	private final AuthenticationManager authenticationManager;

	private final HttpServletRequest request;

	/**
	 * Thực hiện đăng nhập với tên người dùng và mật khẩu. Nếu xác thực thành công,
	 * thông tin người dùng sẽ được lưu trong SecurityContext và session.
	 *
	 * @param username tên đăng nhập
	 * @param password mật khẩu
	 * @return true nếu đăng nhập thành công, ném RuntimeException nếu thất bại
	 */
	@Override
	public boolean login(String username, String password) {
		if (!StringUtils.hasText(username) || !StringUtils.hasText(password)) {
			throw new RuntimeException("Vui lòng nhập đầy đủ thông tin");
		}

		try {
			Authentication authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(username, password));

			SecurityContextHolder.getContext().setAuthentication(authentication);

			HttpSession session = request.getSession(true);
			session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
					SecurityContextHolder.getContext());

			return true;
		} catch (AuthenticationException e) {
			throw new RuntimeException("Tên đăng nhập hoặc mật khẩu không hợp lệ");
		}
	}
}
