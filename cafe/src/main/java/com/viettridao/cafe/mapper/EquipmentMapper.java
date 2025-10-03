package com.viettridao.cafe.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import com.viettridao.cafe.dto.request.equipment.CreateEquipmentRequest;
import com.viettridao.cafe.dto.request.equipment.UpdateEquipmentRequest;
import com.viettridao.cafe.dto.response.equipment.EquipmentResponse;
import com.viettridao.cafe.dto.response.expenses.BudgetViewResponse;
import com.viettridao.cafe.model.EquipmentEntity;

/**
 * Mapper sử dụng MapStruct để chuyển đổi giữa EquipmentEntity, các DTO request
 * và response.
 * 
 * componentModel = "spring" để Spring quản lý bean mapper này, có
 * thể @Autowired trực tiếp.
 */
@Mapper(componentModel = "spring")
public interface EquipmentMapper {

    EquipmentResponse toDto(EquipmentEntity entity);

    List<EquipmentResponse> toDtoList(List<EquipmentEntity> entities);

    @Mappings({
        @Mapping(target = "id", ignore = true),                  // id do DB sinh tự động
        @Mapping(target = "isDeleted", constant = "false"),     // mặc định chưa bị xoá khi tạo mới
        @Mapping(target = "notes", ignore = true)                // bỏ qua field notes nếu không có trong request
    })
    EquipmentEntity fromCreateRequest(CreateEquipmentRequest request);

    @Mappings({
        @Mapping(target = "id", source = "id"),                  // giữ nguyên id khi update
        @Mapping(target = "isDeleted", ignore = true),           // không cập nhật cờ xoá khi update
        @Mapping(target = "notes", ignore = true)                // không cập nhật notes khi update (nếu muốn update thì thêm field notes vào DTO)
    })
    void updateEntityFromUpdateRequest(UpdateEquipmentRequest request, @MappingTarget EquipmentEntity entity);

    @Mapping(target = "date", source = "purchaseDate")
    @Mapping(target = "expense", source = "purchasePrice")
    @Mapping(target = "income", constant = "0.0")
    BudgetViewResponse toBudgetDto(EquipmentEntity entity);
}
