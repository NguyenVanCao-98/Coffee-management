package com.viettridao.cafe.dto.request.employee;

import com.viettridao.cafe.exception.OptionalSize;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateEmployeeRequest {

	@NotBlank(message = "Tên nhân viên không được để trống")
	@Size(min = 5, message = "Tên nhân viên tối thiểu 5 ký tự")
	private String fullName;

	@Pattern(regexp = "^0[0-9]{9,10}$", message = "Số điện thoại phải bắt đầu bằng số 0 và số điện thoại 10-11 chữ số")
	private String phoneNumber;

	@OptionalSize(min = 5, message = "Địa chỉ tối thiểu 5 ký tự nếu được nhập")
	private String address;

	private String imageUrl;

	@NotNull(message = "Chức vụ không được để trống")
	@Min(value = 1, message = "Id chức vụ phải lớn hơn 0")
	private Integer positionId;

	private String positionName;

	@OptionalSize(min = 3, message = "Username tối thiểu 3 ký tự nếu được nhập")
	private String username;

	@OptionalSize(min = 6, message = "Password tối thiểu 6 ký tự nếu được nhập")
	private String password;
}
