package com.viettridao.cafe.dto.request.account;

import com.viettridao.cafe.exception.OptionalSize;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateAccountRequest {

	private Integer id;

	@OptionalSize(min = 5, message = "Họ tên ít nhất 5 ký tự nếu được nhập")
	private String fullName;

	@Pattern(regexp = "^0[0-9]{9,10}$", message = "Số điện thoại phải bắt đầu bằng số 0 và số điện thoại 10-11 chữ số")
	private String phoneNumber;

	@OptionalSize(min = 5, message = "Địa chỉ tối thiểu 5 ký tự nếu được nhập")
	private String address;

	private String imageUrl;

	private Integer positionId;

	private String positionName;

	private Double salary;

	private String username;

	@OptionalSize(min = 6, message = "Password tối thiểu 6 ký tự nếu được nhập")
	private String password;
}
