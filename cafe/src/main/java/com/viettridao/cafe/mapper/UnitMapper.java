package com.viettridao.cafe.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.viettridao.cafe.dto.request.CreateUnitRequest;
import com.viettridao.cafe.dto.response.unit.UnitResponse;
import com.viettridao.cafe.model.UnitEntity;

/**
 * UnitMapper dùng để ánh xạ giữa {@link UnitEntity} và các DTO tương ứng như
 * {@link CreateUnitRequest} và {@link UnitResponse}.
 * 
 * Interface này sử dụng MapStruct để tự động sinh mã chuyển đổi, giúp giảm lỗi
 * và rút gọn thời gian phát triển.
 */
@Mapper(componentModel = "spring")
public interface UnitMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)  // bỏ qua trường isDeleted khi tạo mới
    @Mapping(target = "products", ignore = true)   // bỏ qua quan hệ products khi tạo mới
    UnitEntity fromRequest(CreateUnitRequest request);

    // Ở chiều entity -> DTO thì có thể không cần ignore nếu DTO không có trường đó
    UnitResponse toDto(UnitEntity entity);

    List<UnitResponse> toDtoList(List<UnitEntity> entities);
}
