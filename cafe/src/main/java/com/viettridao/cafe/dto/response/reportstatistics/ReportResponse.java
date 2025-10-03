package com.viettridao.cafe.dto.response.reportstatistics;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportResponse {
	
	private List<ReportItemResponse> items;
	
	private Double totalRevenue;
	
	private Double totalExpense;
}
