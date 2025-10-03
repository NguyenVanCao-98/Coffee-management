package com.viettridao.cafe.dto.response.employee;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDailySalaryResponse {
    private String fullName;
	private LocalDate date;
	private Double dailySalary;
}
