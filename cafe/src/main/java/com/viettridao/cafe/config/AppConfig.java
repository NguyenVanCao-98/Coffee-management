package com.viettridao.cafe.config;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletPath;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.DispatcherServlet;

import com.viettridao.cafe.service.UserServiceDetail;

import lombok.RequiredArgsConstructor;

/**
 * Lớp cấu hình bảo mật cho toàn bộ ứng dụng. Cấu hình này sử dụng Spring
 * Security để kiểm soát truy cập, xác thực người dùng, và mã hoá mật khẩu.
 */
@Configuration // Đánh dấu đây là class cấu hình của Spring
@RequiredArgsConstructor // Lombok tự tạo constructor cho các biến final, dùng để DI
@EnableAsync
public class AppConfig {

	/**
	 * Danh sách các đường dẫn được phép truy cập công khai (không cần đăng nhập).
	 * Ví dụ: trang đăng nhập, file tĩnh (JS, CSS...).
	 */
	private static final String[] AUTH_WHITELIST = { "/login", "/js/**", "/css/**" };

	private final UserServiceDetail userServiceDetail; // Service cung cấp thông tin người dùng

	/**
	 * Cấu hình chuỗi lọc bảo mật chính (Security Filter Chain).
	 * 
	 * - Cho phép truy cập các URL trong whitelist. - Yêu cầu xác thực với các URL
	 * còn lại. - Vô hiệu hóa form login và HTTP Basic mặc định. - Cấu hình đăng
	 * xuất và xoá session.
	 * 
	 * @param http Cấu hình HttpSecurity được Spring inject sẵn.
	 * @return SecurityFilterChain đã cấu hình.
	 * @throws Exception Nếu có lỗi trong quá trình build cấu hình.
	 */
//	@Bean
//	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//		http.csrf(Customizer.withDefaults()) // Bật CSRF protection mặc định
//				.cors(withDefaults()) // Bật cấu hình CORS mặc định
//				.authorizeHttpRequests(request -> request.requestMatchers(AUTH_WHITELIST).permitAll() // Cho phép truy
//																										// cập các URL
//																										// trong
//																										// whitelist
//						.anyRequest().authenticated()) // Yêu cầu xác thực với các URL còn lại
//				.formLogin(Customizer.withDefaults()) // Mở lại form login mặc định
//				.httpBasic(AbstractHttpConfigurer::disable) // Tắt HTTP Basic mặc định
//				.authenticationProvider(authenticationProvider()) // Sử dụng provider xác thực tự định nghĩa
//				.logout(logout -> logout.logoutUrl("/logout") // URL để đăng xuất
//						.logoutSuccessUrl("/login?logout") // URL sau khi logout thành công
//						.invalidateHttpSession(true) // Xoá session khi logout
//						.deleteCookies("JSESSIONID") // Xoá cookie session
//						.permitAll()); // Cho phép tất cả truy cập logout
//		return http.build(); // Trả về cấu hình đã hoàn chỉnh
//	}
//	
	  @Bean
	    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	        http
	                .csrf(Customizer.withDefaults())
	                .cors(withDefaults())
	                .authorizeHttpRequests(request -> request
	                        .requestMatchers(AUTH_WHITELIST).permitAll()
	                        .anyRequest().authenticated())
	                // .formLogin(form -> form
	                //         .loginPage("/login")
	                //         .defaultSuccessUrl("/home", true)
	                //         .permitAll())
	                .httpBasic(AbstractHttpConfigurer::disable)
	                .authenticationProvider(authenticationProvider())
	                .logout(logout -> logout
	                        .logoutUrl("/logout")
	                        .logoutSuccessUrl("/login?logout")
	                        .invalidateHttpSession(true)
	                        .deleteCookies("JSESSIONID")
	                        .permitAll()
	                );

	        return http.build();
	    }

	/**
	 * Bean cấu hình AuthenticationProvider sử dụng DaoAuthenticationProvider để xác
	 * thực người dùng từ cơ sở dữ liệu.
	 * 
	 * @return AuthenticationProvider đã cấu hình.
	 */
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userServiceDetail); // Cung cấp service lấy dữ liệu user
		provider.setPasswordEncoder(passwordEncoder()); // Sử dụng password encoder (BCrypt)
		return provider;
	}

	/**
	 * Bean cung cấp AuthenticationManager để xử lý quá trình xác thực.
	 * 
	 * @param config Đối tượng cấu hình xác thực do Spring cung cấp.
	 * @return AuthenticationManager được tạo từ cấu hình.
	 * @throws Exception Nếu có lỗi khi tạo.
	 */
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager(); // Lấy AuthenticationManager mặc định của Spring
	}

	/**
	 * Bean mã hoá mật khẩu sử dụng thuật toán BCrypt.
	 * 
	 * @return Đối tượng PasswordEncoder sử dụng BCrypt.
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(6); // Tạo password encoder với độ mạnh 6 (hashing complexity)
	}

	/**
	 * Cấu hình đăng ký DispatcherServlet để xử lý request. Bật tính năng ném
	 * exception khi không tìm thấy handler xử lý request.
	 * 
	 * @param dispatcherServlet DispatcherServlet được Spring tạo sẵn
	 * @return ServletRegistrationBean đăng ký servlet
	 */
	@Bean
	public ServletRegistrationBean<DispatcherServlet> dispatcherServletRegistration(
			DispatcherServlet dispatcherServlet) {
		dispatcherServlet.setThrowExceptionIfNoHandlerFound(true); // Bật ném lỗi nếu không tìm thấy handler (giúp xử lý
																	// lỗi 404)
		return new ServletRegistrationBean<>(dispatcherServlet, "/"); // Đăng ký DispatcherServlet xử lý mọi request
	}

	/**
	 * Cung cấp DispatcherServletPath với đường dẫn gốc "/". Hỗ trợ cấu hình các
	 * servlet liên quan đường dẫn.
	 * 
	 * @return DispatcherServletPath trả về đường dẫn "/"
	 */
	@Bean
	public DispatcherServletPath dispatcherServletPath() {
		return new DispatcherServletPath() {
			@Override
			public String getPath() {
				return "/"; // Đường dẫn gốc
			}
		};
	}
}
