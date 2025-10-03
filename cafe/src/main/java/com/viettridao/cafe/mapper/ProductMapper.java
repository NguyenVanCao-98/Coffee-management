package com.viettridao.cafe.mapper;

import java.util.Comparator;

import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import com.viettridao.cafe.dto.request.product.ProductRequest;
import com.viettridao.cafe.dto.response.product.ProductResponse;
import com.viettridao.cafe.model.ExportEntity;
import com.viettridao.cafe.model.ImportEntity;
import com.viettridao.cafe.model.ProductEntity;
import com.viettridao.cafe.model.UnitEntity;
import com.viettridao.cafe.service.UnitService;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class ProductMapper {

	@Autowired
	protected UnitService unitService;

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "unit", ignore = true)
	@Mapping(target = "imports", ignore = true)
	@Mapping(target = "exports", ignore = true)
	@Mapping(target = "isDeleted", ignore = true)
	@Mapping(target = "menuDetails", ignore = true)
	public abstract ProductEntity fromRequest(ProductRequest request);

	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	@Mapping(target = "unit", ignore = true)
	@Mapping(target = "exports", ignore = true)
	@Mapping(target = "imports", ignore = true)
	@Mapping(target = "isDeleted", ignore = true)
	@Mapping(target = "menuDetails", ignore = true)
	public abstract void updateEntityFromRequest(ProductRequest request, @MappingTarget ProductEntity entity);

	@AfterMapping
	protected void afterMappingRequest(ProductRequest request, @MappingTarget ProductEntity entity) {
		if (request.getUnitId() != null && request.getUnitId() > 0
				&& (entity.getUnit() == null || !request.getUnitId().equals(entity.getUnit().getId()))) {

			// Lấy đầy đủ UnitEntity từ DB, ném exception nếu không tìm thấy
			UnitEntity unit = unitService.findById(request.getUnitId());
			if (unit != null) {
				entity.setUnit(unit);
			}
		}
	}

	@Mapping(target = "unitId", source = "unit.id")
	@Mapping(target = "unitName", source = "unit.unitName")
	@Mapping(target = "latestPrice", ignore = true)
	@Mapping(target = "lastImportDate", ignore = true)
	@Mapping(target = "lastExportDate", ignore = true)
	@Mapping(target = "totalAmount", ignore = true)
	@Mapping(target = "currentQuantity", source = "quantity")
	@Mapping(target = "importDate", ignore = true)
	public abstract ProductResponse toResponse(ProductEntity entity);

	@Mapping(target = "quantity", source = "quantity")
	@Mapping(target = "price", ignore = true)
	@Mapping(target = "importDate", ignore = true)
	@Mapping(target = "unitId", source = "unit.id")
	public abstract ProductRequest toRequest(ProductEntity entity);

	@AfterMapping
	protected void afterMappingToResponse(ProductEntity entity, @MappingTarget ProductResponse response) {
		// Lấy giá nhập mới nhất
		if (entity.getImports() != null && !entity.getImports().isEmpty()) {
			entity.getImports().stream().filter(i -> !Boolean.TRUE.equals(i.getIsDeleted()))
					.max(Comparator.comparing(ImportEntity::getImportDate)).ifPresent(latest -> {
						response.setLatestPrice(latest.getPrice());
						response.setLastImportDate(latest.getImportDate());
						response.setImportDate(latest.getImportDate());
					});
		}

		// Lấy xuất cuối cùng
		if (entity.getExports() != null && !entity.getExports().isEmpty()) {
			entity.getExports().stream().filter(e -> !Boolean.TRUE.equals(e.getIsDeleted()))
					.max(Comparator.comparing(ExportEntity::getExportDate))
					.ifPresent(latest -> response.setLastExportDate(latest.getExportDate()));
		}

		// Tính tổng tiền
		if (entity.getQuantity() != null && response.getLatestPrice() != null) {
			response.setTotalAmount(entity.getQuantity() * response.getLatestPrice());
		} else {
			response.setTotalAmount(0.0);
		}

		// Đảm bảo unitName không null
		if (entity.getUnit() != null) {
			response.setUnitName(entity.getUnit().getUnitName());
		}
	}
}
