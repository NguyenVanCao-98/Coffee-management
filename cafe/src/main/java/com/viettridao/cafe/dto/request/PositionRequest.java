package com.viettridao.cafe.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PositionRequest {

	@NotBlank(message = "Tên chức vụ không được để trống")
	private String positionName;

	@NotNull(message = "Lương không được để trống")
	@Positive(message = "Lương phải lớn hơn 0")
	private Double salary;
}
