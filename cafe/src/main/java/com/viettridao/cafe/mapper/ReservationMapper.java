package com.viettridao.cafe.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.viettridao.cafe.dto.request.tables.TableBookingRequest;
import com.viettridao.cafe.dto.response.tables.TableBookingResponse;
import com.viettridao.cafe.model.EmployeeEntity;
import com.viettridao.cafe.model.ReservationEntity;
import com.viettridao.cafe.model.TableEntity;

/**
 * ReservationMapper chịu trách nhiệm ánh xạ giữa các đối tượng: - Từ
 * {@link TableBookingRequest} (DTO phía client) sang {@link ReservationEntity}
 * (Entity lưu trữ DB) - Từ {@link ReservationEntity} sang
 * {@link TableBookingResponse} (DTO trả về client)
 * 
 * Sử dụng MapStruct để tự động tạo code ánh xạ, giúp giảm thiểu boilerplate
 * code.
 */
@Mapper(componentModel = "spring")
public abstract class ReservationMapper {

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "isDeleted", constant = "false")
	@Mapping(target = "table", ignore = true)
	@Mapping(target = "employee", ignore = true)
	@Mapping(target = "invoice", ignore = true)
	public abstract ReservationEntity fromRequest(TableBookingRequest request);

	@AfterMapping
	protected void afterMapping(TableBookingRequest request, @MappingTarget ReservationEntity entity) {
		if (request.getTableId() != null) {
			TableEntity table = new TableEntity();
			table.setId(request.getTableId());
			entity.setTable(table);
		}
		if (request.getEmployeeId() != null) {
			EmployeeEntity employee = new EmployeeEntity();
			employee.setId(request.getEmployeeId());
			entity.setEmployee(employee);
		}
		// Nếu bạn có invoiceId trong request thì cũng set tương tự
	}

	@Mapping(target = "success", ignore = true)
	@Mapping(target = "message", ignore = true)
	public abstract TableBookingResponse toDto(ReservationEntity entity);
}
