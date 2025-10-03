package com.viettridao.cafe.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import com.viettridao.cafe.dto.request.employee.CreateEmployeeRequest;
import com.viettridao.cafe.dto.request.employee.UpdateEmployeeRequest;
import com.viettridao.cafe.dto.response.employee.EmployeeResponse;
import com.viettridao.cafe.model.EmployeeEntity;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

	// Entity -> DTO
	@Mappings({ @Mapping(source = "position.id", target = "positionId"),
			@Mapping(source = "position.positionName", target = "positionName"),
			@Mapping(source = "position.salary", target = "salary"),
			@Mapping(source = "account.username", target = "username"),
			@Mapping(source = "account.password", target = "password"),
			@Mapping(source = "account.imageUrl", target = "imageUrl") })
	EmployeeResponse toDto(EmployeeEntity entity);

	List<EmployeeResponse> toDtoList(List<EmployeeEntity> entities);

	// Create -> Entity
	@Mappings({
	    @Mapping(target = "id", ignore = true),
	    @Mapping(target = "isDeleted", constant = "false"), // mặc định false khi tạo mới
	    @Mapping(target = "position.id", source = "positionId"),
	    @Mapping(target = "position.positionName", source = "positionName"),
	    @Mapping(target = "account.username", source = "username"),
	    @Mapping(target = "account.password", source = "password"),
	    @Mapping(target = "account.imageUrl", source = "imageUrl"),
	    @Mapping(target = "imports", ignore = true),
	    @Mapping(target = "exports", ignore = true),
	    @Mapping(target = "reservations", ignore = true)
	})
	EmployeeEntity fromCreateRequest(CreateEmployeeRequest dto);

	// Update -> Entity
	@Mappings({
	    @Mapping(target = "id", source = "id"),
	    @Mapping(target = "isDeleted", ignore = true), // không cập nhật cờ xoá khi update
	    @Mapping(target = "position.id", source = "positionId"),
	    @Mapping(target = "position.positionName", source = "positionName"),
	    @Mapping(target = "account.username", source = "username"),
	    @Mapping(target = "account.password", source = "password"),
	    @Mapping(target = "account.imageUrl", source = "imageUrl"),
	    @Mapping(target = "imports", ignore = true),
	    @Mapping(target = "exports", ignore = true),
	    @Mapping(target = "reservations", ignore = true)
	})
	void updateEntityFromUpdateRequest(UpdateEmployeeRequest dto, @MappingTarget EmployeeEntity entity);
}