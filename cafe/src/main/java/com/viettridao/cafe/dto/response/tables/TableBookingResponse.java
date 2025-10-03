package com.viettridao.cafe.dto.response.tables;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TableBookingResponse {
	
	private boolean success;
	
	private String message;

	public TableBookingResponse() {
	}

	public TableBookingResponse(boolean success, String message) {
		this.success = success;
		this.message = message;
	}

}
