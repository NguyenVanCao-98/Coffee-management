package com.viettridao.cafe.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "positions") // chucvu
public class PositionEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "position_id")
	private Integer id;

	@Column(name = "salary")
	private Double salary;

	@Column(name = "position_name")
	private String positionName;

	@Column(name = "is_deleted")
	private Boolean isDeleted;

	@OneToMany(mappedBy = "position")
	private List<EmployeeEntity> employees;
}
