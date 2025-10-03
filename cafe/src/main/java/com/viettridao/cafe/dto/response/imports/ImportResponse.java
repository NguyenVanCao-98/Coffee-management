package com.viettridao.cafe.dto.response.imports;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImportResponse {

	private Integer id;

	private Integer productId;

	private String productName;

	private Integer quantity;

	private Double totalAmount;

	private LocalDate importDate;

	private String employeeName;
	
	private Double price;


}
