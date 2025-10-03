package com.viettridao.cafe.dto.request.export;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExportRequest {

	@NotNull(message = "ID sản phẩm là bắt buộc")
	private Integer productId;

	@NotNull(message = "Số lượng xuất là bắt buộc")
	@Min(value = 1, message = "Số lượng phải lớn hơn 0")
	private Integer quantity;

	@NotNull(message = "Ngày xuất là bắt buộc")
	@PastOrPresent(message = "Ngày xuất không được ở tương lai")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate exportDate;
}
