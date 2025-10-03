package com.viettridao.cafe.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.viettridao.cafe.dto.request.export.ExportRequest;
import com.viettridao.cafe.dto.response.exports.ExportResponse;
import com.viettridao.cafe.model.ExportEntity;

/**
 * Mapper sử dụng MapStruct để chuyển đổi giữa ExportEntity, DTO request và
 * response.
 * 
 * componentModel = "spring" để Spring quản lý bean mapper này, có
 * thể @Autowired trực tiếp.
 */
@Mapper(componentModel = "spring")
public interface ExportMapper {

	@Mappings({ @Mapping(source = "product.id", target = "productId"),
			@Mapping(source = "product.productName", target = "productName"),
			@Mapping(target = "employeeName", expression = "java(entity.getEmployee() != null ? entity.getEmployee().getFullName() : \"Không xác định\")") })
	ExportResponse toDto(ExportEntity entity);

	@Mappings({ @Mapping(target = "id", ignore = true), @Mapping(target = "product", ignore = true),
			@Mapping(target = "employee", ignore = true), @Mapping(target = "isDeleted", constant = "false") // mặc định
																												// chưa
																												// xóa
	})
	ExportEntity fromRequest(ExportRequest request);
}
