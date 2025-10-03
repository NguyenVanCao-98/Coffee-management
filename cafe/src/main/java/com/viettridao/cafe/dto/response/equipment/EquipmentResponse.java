package com.viettridao.cafe.dto.response.equipment;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EquipmentResponse {
	private Integer id;

	private String equipmentName;

	private Integer quantity;

	private String notes;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate purchaseDate;

	private Double purchasePrice;

	private Boolean isDeleted;

}
