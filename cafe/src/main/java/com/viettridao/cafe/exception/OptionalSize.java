package com.viettridao.cafe.exception;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * Annotation tùy chỉnh dùng để kiểm tra kích thước (độ dài) của một trường dữ
 * liệu chỉ khi trường đó được nhập (không null hoặc không rỗng). Nếu trường
 * không được nhập (null hoặc rỗng) thì không kiểm tra.
 * 
 * Áp dụng được cho phương thức (method) hoặc trường (field).
 */
@Documented
@Constraint(validatedBy = OptionalSizeValidator.class) // Validator thực thi logic kiểm tra
@Target({ ElementType.METHOD, ElementType.FIELD }) // Có thể dùng cho method hoặc field
@Retention(RetentionPolicy.RUNTIME) // Annotation tồn tại ở runtime để validator truy cập được
public @interface OptionalSize {

	/**
	 * Thông báo lỗi mặc định nếu validation thất bại
	 */
	String message() default "Trường phải có tối thiểu ký tự nếu được nhập";

	/**
	 * Độ dài tối thiểu của chuỗi nếu trường được nhập
	 */
	int min() default 0;

	/**
	 * Độ dài tối đa của chuỗi nếu trường được nhập
	 */
	int max() default Integer.MAX_VALUE;

	/**
	 * Các nhóm validation (thường để phân loại nhóm kiểm tra)
	 */
	Class<?>[] groups() default {};

	/**
	 * Payload cho thông tin metadata về validation
	 */
	Class<? extends Payload>[] payload() default {};
}
