package com.viettridao.cafe.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.viettridao.cafe.dto.request.PositionRequest;
import com.viettridao.cafe.dto.response.position.PositionResponse;
import com.viettridao.cafe.model.PositionEntity;

/**
 * PositionMapper dùng để ánh xạ giữa PositionEntity và các lớp DTO liên quan.
 * Sử dụng MapStruct để tự động sinh code ánh xạ.
 * 
 * componentModel = "spring" cho phép Spring quản lý bean này và tự động inject
 * khi cần.
 */
@Mapper(componentModel = "spring")
public interface PositionMapper {

	/**
	 * Map từ request (client gửi lên) sang entity (lưu DB). Bỏ qua id và isDeleted
	 * vì id do DB tự sinh, isDeleted mặc định false khi tạo mới.
	 */
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "isDeleted", ignore = true)
	@Mapping(target = "employees", ignore = true) // tránh load list nhân viên khi không cần
	PositionEntity fromRequest(PositionRequest request);

	/**
	 * Map từ entity sang DTO trả về client. Không trả về employees và isDeleted.
	 */
	PositionResponse toDto(PositionEntity entity);

	/**
	 * Map list entity sang list DTO.
	 */
	List<PositionResponse> toDtoList(List<PositionEntity> entities);
}