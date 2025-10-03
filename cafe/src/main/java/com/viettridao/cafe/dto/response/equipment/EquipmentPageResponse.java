package com.viettridao.cafe.dto.response.equipment;

import java.util.List;

import com.viettridao.cafe.dto.response.PageResponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EquipmentPageResponse extends PageResponse {
	private List<EquipmentResponse> equipments;
}
