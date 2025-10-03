package com.viettridao.cafe.dto.request.product;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductSearchRequest {
	@NotBlank(message = "Vui lòng nhập từ khóa tìm kiếm")
	private String keyword;
}
