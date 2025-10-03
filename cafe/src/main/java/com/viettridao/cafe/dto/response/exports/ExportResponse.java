package com.viettridao.cafe.dto.response.exports;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExportResponse {

	private Integer id;

	private Integer productId;

	private String productName;

	private Integer quantity;

	private Double totalExportAmount;

	private LocalDate exportDate;

	private String employeeName;
	
}
