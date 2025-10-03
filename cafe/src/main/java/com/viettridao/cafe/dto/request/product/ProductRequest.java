package com.viettridao.cafe.dto.request.product;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequest {

	private Integer id;

	@NotBlank(message = "Tên sản phẩm không được để trống")
	private String productName;

	@NotNull(message = "Số lượng không được để trống")
	@Min(value = 1, message = "Số lượng phải lớn hơn 0")
	private Integer quantity;

	@NotNull(message = "Đơn vị tính không được để trống")
	private Integer unitId;

	@NotNull(message = "Ngày nhập không được để trống")
	@PastOrPresent(message = "Ngày nhập không được là tương lai")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate importDate;

	@NotNull(message = "Giá sản phẩm không được để trống")
	@Positive(message = "Giá sản phẩm phải lớn hơn 0")
	private Double price;
	
	private LocalDate lastImportDate;

}
