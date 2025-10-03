package com.viettridao.cafe.dto.request.tables;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuItemSplitRequest {

	@NotNull(message = "ID món không được để trống")
	private Integer menuItemId;

	@NotNull(message = "Số lượng tách không được để trống")
	@Min(value = 1, message = "Số lượng tách phải lớn hơn 0")
	private Integer quantityToMove;

	@NotNull(message = "Trạng thái chọn không được để trống")
	private Boolean selected;
}
