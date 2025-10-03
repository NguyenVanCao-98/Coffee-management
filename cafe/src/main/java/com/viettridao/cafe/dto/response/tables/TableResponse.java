package com.viettridao.cafe.dto.response.tables;

import com.viettridao.cafe.common.TableStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TableResponse {

	private Integer id;

	private String tableName;

	private TableStatus status;

	private Integer invoiceId;

}
