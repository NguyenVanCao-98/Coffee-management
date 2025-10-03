package com.viettridao.cafe.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUnitRequest {

	@NotBlank(message = "Tên đơn vị tính không được để trống")
	private String unitName;
}
