package com.viettridao.cafe.mapper;

import java.util.List;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import com.viettridao.cafe.dto.request.invoices.InvoiceItemRequest;
import com.viettridao.cafe.dto.response.invoices.InvoiceItemResponse;
import com.viettridao.cafe.model.InvoiceDetailEntity;
import com.viettridao.cafe.model.InvoiceKey;

@Mapper(componentModel = "spring")
public interface InvoiceDetailMapper {

	@Mappings({ @Mapping(source = "id.idInvoice", target = "id"),
			@Mapping(source = "menuItem.itemName", target = "itemName"),
			@Mapping(source = "quantity", target = "quantity"), @Mapping(source = "price", target = "unitPrice"),
			@Mapping(target = "totalPrice", expression = "java(entity.getQuantity() * entity.getPrice())") })
	InvoiceItemResponse toDto(InvoiceDetailEntity entity);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "menuItem", ignore = true)
	@Mapping(target = "price", ignore = true)
	@Mapping(target = "isDeleted", ignore = true)
	@Mapping(target = "invoice", ignore = true) // Thêm dòng này để tránh lỗi
	InvoiceDetailEntity fromRequest(InvoiceItemRequest request);

	@AfterMapping
	default void afterMapping(InvoiceItemRequest request, @MappingTarget InvoiceDetailEntity entity) {
		InvoiceKey key = new InvoiceKey();
		key.setIdInvoice(request.getInvoiceId());
		key.setIdMenuItem(request.getMenuItemId());
		entity.setId(key);
		entity.setQuantity(request.getQuantity());
		entity.setIsDeleted(false);
	}

	List<InvoiceItemResponse> toDtoList(List<InvoiceDetailEntity> entities);
}
