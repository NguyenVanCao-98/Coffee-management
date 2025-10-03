package com.viettridao.cafe.dto.response.menu;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MenuDetailResponse {
	private Integer productId;
	private String productName;
	private Double quantity;
	private String unitName;
}
