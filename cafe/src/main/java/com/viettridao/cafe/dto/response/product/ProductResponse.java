package com.viettridao.cafe.dto.response.product;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
	private Integer id;
	
	private String productName;
	
	private String unitName;
	
	private Integer currentQuantity;
	
	private Double latestPrice;
	
	private LocalDate lastImportDate;
	
	private LocalDate lastExportDate;
	
	private Double totalAmount;
	
	private Integer unitId;    
	
	private LocalDate importDate;
	
}
