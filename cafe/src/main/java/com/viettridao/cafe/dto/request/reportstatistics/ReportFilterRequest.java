package com.viettridao.cafe.dto.request.reportstatistics;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportFilterRequest {

	@NotNull(message = "Ngày bắt đầu không được để trống")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	@PastOrPresent(message = "Ngày bắt đầu phải là ngày hiện tại hoặc trong quá khứ")
	private LocalDate fromDate;

	@NotNull(message = "Ngày kết thúc không được để trống")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	@PastOrPresent(message = "Ngày kết thúc phải là ngày hiện tại hoặc trong quá khứ")
	private LocalDate toDate;

	private String category;

	public boolean isValidDateRange() {
		return fromDate != null && toDate != null && !fromDate.isAfter(toDate);
	}
}
