package com.viettridao.cafe.mapper;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.viettridao.cafe.dto.request.menu.MenuDetailRequest;
import com.viettridao.cafe.dto.request.menu.MenuItemRequest;
import com.viettridao.cafe.dto.response.menu.MenuDetailResponse;
import com.viettridao.cafe.dto.response.menu.MenuItemResponse;
import com.viettridao.cafe.model.MenuDetailEntity;
import com.viettridao.cafe.model.MenuItemEntity;

@Mapper(componentModel = "spring")
public interface MenuItemMapper {

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "isDeleted", constant = "false")
	@Mapping(target = "menuDetails", source = "ingredients")
	MenuItemEntity toEntity(MenuItemRequest request);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "menuItem", ignore = true)
	@Mapping(target = "product.id", source = "productId")
	@Mapping(target = "product", ignore = true)
	@Mapping(target = "isDeleted", constant = "false")
	MenuDetailEntity toEntity(MenuDetailRequest request);

	List<MenuDetailEntity> toDetailEntities(List<MenuDetailRequest> requests);

	@Mapping(target = "ingredients", source = "menuDetails")
	MenuItemResponse toResponse(MenuItemEntity entity);

	@Mapping(target = "productId", source = "product.id")
	@Mapping(target = "productName", source = "product.productName")
	MenuDetailResponse toResponse(MenuDetailEntity entity);

	List<MenuItemResponse> toResponses(List<MenuItemEntity> entities);

	List<MenuDetailResponse> toDetailResponses(List<MenuDetailEntity> entities);

	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "isDeleted", ignore = true)
	@Mapping(target = "menuDetails", ignore = true)
	void updateEntityFromRequest(MenuItemRequest request, @MappingTarget MenuItemEntity entity);
}
