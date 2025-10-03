package com.viettridao.cafe.dto.response.promotion;

import java.util.List;

import com.viettridao.cafe.dto.response.PageResponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PromotionPageResponse extends PageResponse {
	
	private List<PromotionResponse> promotions;
}
