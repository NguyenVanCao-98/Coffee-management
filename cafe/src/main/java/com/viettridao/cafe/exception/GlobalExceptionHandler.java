package com.viettridao.cafe.exception;

import java.util.stream.Collectors;

import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import jakarta.validation.ConstraintViolationException;

/**
 * Lớp xử lý ngoại lệ toàn cục cho ứng dụng Spring MVC. Dùng @ControllerAdvice
 * để bắt và xử lý các lỗi từ controller trả về trang lỗi tương ứng với từng
 * loại ngoại lệ.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * Xử lý lỗi 404 khi không tìm thấy trang yêu cầu. Bắt ngoại lệ
	 * NoHandlerFoundException và trả về view error/404.
	 * 
	 * @param ex    ngoại lệ NoHandlerFoundException
	 * @param model đối tượng Model để truyền dữ liệu sang view
	 * @return tên view hiển thị trang lỗi 404
	 */
	@ExceptionHandler(NoHandlerFoundException.class)
	public String handleNotFound(NoHandlerFoundException ex, Model model) {
		model.addAttribute("error", "Không tìm thấy trang bạn yêu cầu.");
		model.addAttribute("message", ex.getMessage());
		return "error/404";
	}

	/**
	 * Xử lý lỗi validation khi sử dụng @Valid mà không có BindingResult, ngoại lệ
	 * MethodArgumentNotValidException sẽ được bắt ở đây. Tổng hợp các lỗi của từng
	 * trường dữ liệu và hiển thị chi tiết.
	 * 
	 * @param ex    ngoại lệ MethodArgumentNotValidException
	 * @param model đối tượng Model để truyền dữ liệu sang view
	 * @return tên view hiển thị trang lỗi validation
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public String handleValidationException(MethodArgumentNotValidException ex, Model model) {
		String errorMessages = ex.getBindingResult().getFieldErrors().stream()
				.map(error -> error.getField() + ": " + error.getDefaultMessage()).collect(Collectors.joining("<br/>"));

		model.addAttribute("error", "Dữ liệu không hợp lệ");
		model.addAttribute("message", errorMessages);
		return "error/validation";
	}

	/**
	 * Xử lý lỗi vi phạm ràng buộc khi sử dụng @PathVariable hoặc @RequestParam,
	 * ngoại lệ ConstraintViolationException sẽ được bắt ở đây. Tổng hợp các thông
	 * báo vi phạm và trả về view lỗi validation.
	 * 
	 * @param ex    ngoại lệ ConstraintViolationException
	 * @param model đối tượng Model để truyền dữ liệu sang view
	 * @return tên view hiển thị trang lỗi validation
	 */
	@ExceptionHandler(ConstraintViolationException.class)
	public String handleConstraintViolation(ConstraintViolationException ex, Model model) {
		String errorMessages = ex.getConstraintViolations().stream()
				.map(cv -> cv.getPropertyPath() + ": " + cv.getMessage()).collect(Collectors.joining("<br/>"));

		model.addAttribute("error", "Vi phạm ràng buộc dữ liệu");
		model.addAttribute("message", errorMessages);
		return "error/validation";
	}

	/**
	 * Xử lý các lỗi hệ thống chung không được xử lý bởi các handler trên. Bắt tất
	 * cả ngoại lệ Exception và trả về trang lỗi 500.
	 * 
	 * @param ex    ngoại lệ Exception
	 * @param model đối tượng Model để truyền dữ liệu sang view
	 * @return tên view hiển thị trang lỗi 500
	 */
	@ExceptionHandler(Exception.class)
	public String handleGeneralException(Exception ex, Model model) {
		model.addAttribute("error", "Lỗi hệ thống");
		model.addAttribute("message", ex.getMessage());
		return "error/500";
	}
}
