package com.viettridao.cafe.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.viettridao.cafe.dto.request.promotion.CreatePromotionRequest;
import com.viettridao.cafe.dto.response.promotion.PromotionResponse;
import com.viettridao.cafe.model.PromotionEntity;

@Mapper(componentModel = "spring")
public interface PromotionMapper {

    /**
     * Map từ CreatePromotionRequest sang PromotionEntity.
     * - Bỏ qua id vì DB sẽ tự sinh.
     * - Bỏ qua isDeleted để mặc định false khi tạo mới.
     * - Bỏ qua invoices để tránh map quan hệ khi không cần.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "invoices", ignore = true)
    PromotionEntity fromRequest(CreatePromotionRequest request);

    /**
     * Map từ PromotionEntity sang PromotionResponse.
     * Các field trùng tên sẽ tự map.
     */
    PromotionResponse toDto(PromotionEntity entity);

    /**
     * Map list entity sang list DTO.
     */
    List<PromotionResponse> toDtoList(List<PromotionEntity> entities);
}
