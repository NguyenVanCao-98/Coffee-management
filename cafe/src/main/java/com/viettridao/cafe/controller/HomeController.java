package com.viettridao.cafe.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

/**
 * Controller chịu trách nhiệm điều hướng tới trang giao diện chính (layout).
 */
@Controller
@RequiredArgsConstructor
public class HomeController {

	/**
	 * Điều hướng đến trang layout chính của hệ thống.
	 * 
	 * @return tên view "layout", được sử dụng để hiển thị giao diện tổng quát
	 *         (thường là template chính chứa các phần header, sidebar, content,
	 *         footer...).
	 */
	@GetMapping("/home")
	public String home() {
		return "layout";
	}
}
