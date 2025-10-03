package com.viettridao.cafe.dto.request.menu;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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
public class MenuItemRequest {

	@NotBlank(message = "Tên món không được để trống")
	@Size(max = 50, message = "Tên món tối đa 50 ký tự")
	private String itemName;

	@NotNull(message = "Giá tiền không được để trống")
	@DecimalMin(value = "1000", message = "Giá tiền phải lớn hơn 1000")
	private Double currentPrice;

	@NotEmpty(message = "Phải có ít nhất 1 thành phần")
	private List<@Valid MenuDetailRequest> ingredients;
}
