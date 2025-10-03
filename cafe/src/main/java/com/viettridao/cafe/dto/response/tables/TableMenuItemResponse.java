package com.viettridao.cafe.dto.response.tables;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TableMenuItemResponse {
	
	private Integer menuItemId;
	
	private String itemName;
	
	private Integer quantity;
	
	private Double price;
	
	private Double amount;
}
