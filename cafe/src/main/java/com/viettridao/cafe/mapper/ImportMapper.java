package com.viettridao.cafe.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import com.viettridao.cafe.dto.request.imports.ImportRequest;
import com.viettridao.cafe.dto.response.imports.ImportResponse;
import com.viettridao.cafe.model.ImportEntity;
import com.viettridao.cafe.model.ProductEntity;

/**
 * Mapper sử dụng MapStruct để chuyển đổi giữa ImportEntity, DTO request và
 * response.
 * 
 * componentModel = "spring" để Spring quản lý bean mapper này, có
 * thể @Autowired trực tiếp.
 */
@Mapper(componentModel = "spring")
public interface ImportMapper {

    @Mappings({
        @Mapping(source = "product.id", target = "productId"),
        @Mapping(source = "product.productName", target = "productName", defaultValue = "Không xác định"),
        @Mapping(source = "employee.fullName", target = "employeeName", defaultValue = "Không xác định"),
        @Mapping(source = "price", target = "price")
    })
    ImportResponse toDto(ImportEntity entity);

    @Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "employee", ignore = true),         // set thủ công trong service
        @Mapping(target = "product", ignore = true),          // set thủ công ở @AfterMapping
        @Mapping(target = "totalAmount", ignore = true),
        @Mapping(target = "isDeleted", constant = "false")    // mặc định chưa bị xoá
    })
    ImportEntity fromRequest(ImportRequest request);

    @AfterMapping
    default void afterFromRequest(ImportRequest request, @MappingTarget ImportEntity entity) {
        if (request.getProductId() != null) {
            ProductEntity product = new ProductEntity();
            product.setId(request.getProductId());
            entity.setProduct(product);
        }

        if (request.getQuantity() != null && request.getPrice() != null) {
            entity.setTotalAmount(request.getQuantity() * request.getPrice());
        }
    }
}
