package com.viettridao.cafe.dto.response.employee;

import java.util.List;

import com.viettridao.cafe.dto.response.PageResponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeePageResponse extends PageResponse {
	private List<EmployeeResponse> employees;
}
