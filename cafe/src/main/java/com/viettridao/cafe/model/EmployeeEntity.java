package com.viettridao.cafe.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

//Đánh dấu đây là một thực thể JPA (được ánh xạ với bảng trong cơ sở dữ liệu)
@Entity

//Tự động sinh các getter cho tất cả thuộc tính
@Getter

//Tự động sinh các setter cho tất cả thuộc tính
@Setter

//Ánh xạ lớp này với bảng có tên "employees" trong CSDL
@Table(name = "employees") // Bảng nhân viên
public class EmployeeEntity {

 // Khóa chính của bảng, tự động tăng
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "employee_id") // Cột trong CSDL
	private Integer id;

 // Họ và tên nhân viên
	@Column(name = "full_name")
	private String fullName;

 // Số điện thoại
	@Column(name = "phone_number")
	private String phoneNumber;

 // Địa chỉ
	@Column(name = "address")
	private String address;

 // Cờ đánh dấu nhân viên đã xoá mềm hay chưa (true = đã xoá, false = còn hoạt động)
	@Column(name = "is_deleted")
	private Boolean isDeleted;

 // Quan hệ 1-1 giữa nhân viên và tài khoản (mỗi nhân viên có 1 tài khoản riêng)
 // Cascade.ALL: khi thao tác với nhân viên sẽ tự động áp dụng với tài khoản
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "account_id") // Khóa ngoại tham chiếu đến account
	private AccountEntity account;

 // Quan hệ nhiều nhân viên thuộc 1 chức vụ (nhiều-1)
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "position_id") // Khóa ngoại đến chức vụ
	private PositionEntity position;

 // Quan hệ 1-nhiều: một nhân viên có thể thực hiện nhiều lần nhập hàng
	@OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
	private List<ImportEntity> imports;

 // Quan hệ 1-nhiều: một nhân viên có thể thực hiện nhiều lần xuất hàng
	@OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
	private List<ExportEntity> exports;

 // Quan hệ 1-nhiều: một nhân viên có thể xử lý nhiều đơn đặt bàn
	@OneToMany(mappedBy = "employee")
	private List<ReservationEntity> reservations;
}
