package com.viettridao.cafe.dto.response.reportstatistics;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportItemResponse {
	
	private LocalDate date;
	
	private Double revenue;
	
	private Double expense;
	
	private Double profit;

	public ReportItemResponse(LocalDate date, Double revenue, Double expense) {
		this.date = date;
		this.revenue = revenue;
		this.expense = expense;
		this.profit = revenue - expense;
	}
}
