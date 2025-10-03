package com.viettridao.cafe.dto.response.invoices;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvoiceItemResponse {

	private Integer id;

	private String itemName;

	private Integer quantity;

	private Double unitPrice;

	private Double totalPrice;

}
