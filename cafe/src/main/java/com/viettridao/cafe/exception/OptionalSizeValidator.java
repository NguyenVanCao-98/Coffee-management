package com.viettridao.cafe.exception;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator tùy chỉnh cho annotation @OptionalSize. Kiểm tra độ dài chuỗi chỉ
 * khi giá trị không null và không rỗng. Nếu chuỗi null hoặc rỗng thì coi là hợp
 * lệ.
 */
public class OptionalSizeValidator implements ConstraintValidator<OptionalSize, String> {

	// Độ dài tối thiểu được lấy từ annotation
	private int min;

	// Độ dài tối đa được lấy từ annotation
	private int max;

	/**
	 * Phương thức khởi tạo validator, lấy giá trị min và max từ annotation
	 */
	@Override
	public void initialize(OptionalSize constraintAnnotation) {
		this.min = constraintAnnotation.min();
		this.max = constraintAnnotation.max();
	}

	/**
	 * Phương thức kiểm tra giá trị chuỗi có hợp lệ hay không
	 * 
	 * @param value   giá trị chuỗi cần kiểm tra
	 * @param context ngữ cảnh của validator (thường không dùng)
	 * @return true nếu chuỗi null hoặc rỗng hoặc độ dài nằm trong khoảng min-max;
	 *         false nếu không thỏa mãn
	 */
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		// Nếu giá trị null hoặc chuỗi trắng thì không kiểm tra, coi là hợp lệ
		if (value == null || value.trim().isEmpty()) {
			return true;
		}
		// Kiểm tra độ dài chuỗi nằm trong khoảng từ min đến max
		int length = value.length();
		return length >= min && length <= max;
	}
}
