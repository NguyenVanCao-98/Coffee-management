package com.viettridao.cafe.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageResponse {

	private long pageNumber;

	private long pageSize;

	private long totalPages;

	private long totalElements;
}
