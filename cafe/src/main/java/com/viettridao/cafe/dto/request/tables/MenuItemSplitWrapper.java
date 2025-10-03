package com.viettridao.cafe.dto.request.tables;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuItemSplitWrapper {

	@NotNull(message = "Danh sách món tách không được để trống")
	@Valid
	private List<MenuItemSplitRequest> items;
	
}
