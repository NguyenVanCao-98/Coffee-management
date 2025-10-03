package com.viettridao.cafe.dto.response.menu;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemResponse {
	private Integer id;
	private String itemName;
	private Double currentPrice;
	private List<MenuDetailResponse> ingredients;
}
