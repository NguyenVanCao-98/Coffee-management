package com.viettridao.cafe.dto.request.tables;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TableBookingRequest {

	@NotNull(message = "Id bàn không được để trống")
	private Integer tableId;

	@NotBlank(message = "Tên khách hàng không được để trống")
	@Size(max = 20, message = "Tên khách hàng tối đa 20 ký tự")
	private String customerName;


	@NotBlank(message = "Số điện thoại không được để trống")
	@Pattern(regexp = "\\d{10,11}", message = "Số điện thoại phải gồm 10 hoặc 11 chữ số")
	private String customerPhone;

	@NotNull(message = "Ngày đặt bàn không được để trống")
	@FutureOrPresent(message = "Ngày đặt phải là hôm nay hoặc tương lai")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate reservationDate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
	@NotNull(message = "Giờ đặt bàn không được để trống")
	private LocalTime reservationTime;

	@NotNull(message = "Nhân viên không được để trống")
	private Integer employeeId;

}
