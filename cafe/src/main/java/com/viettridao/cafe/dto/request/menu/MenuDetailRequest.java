package com.viettridao.cafe.dto.request.menu;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MenuDetailRequest {

	@NotNull(message = "Nguyên liệu không được để trống")
	private Integer productId;

	@NotNull(message = "Khối lượng không được để trống")
	@DecimalMin(value = "0.1", message = "Khối lượng phải lớn hơn 0")
	@DecimalMax(value = "1000", message = "Khối lượng không được vượt quá 1000")
	private Double quantity;

	@NotBlank(message = "Đơn vị tính không được để trống")
	@Size(max = 20, message = "Đơn vị tính tối đa 20 ký tự")
	private String unitName;
}
