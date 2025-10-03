package com.viettridao.cafe.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.viettridao.cafe.dto.request.Pay.PaymentRequest;
import com.viettridao.cafe.dto.response.Pay.PaymentResponse;
import com.viettridao.cafe.model.InvoiceEntity;

/**
 * Mapper sử dụng MapStruct để chuyển đổi giữa InvoiceEntity và các DTO liên
 * quan đến Payment.
 * 
 * componentModel = "spring" để Spring quản lý bean mapper này, có
 * thể @Autowired trực tiếp.
 */
@Mapper(componentModel = "spring")
public interface PaymentMapper {

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "totalAmount", ignore = true) // Không map tự động, set thủ công
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "status", ignore = true)
	@Mapping(target = "isDeleted", ignore = true)
	@Mapping(target = "promotion", ignore = true)
	@Mapping(target = "invoiceDetails", ignore = true)
	@Mapping(target = "reservations", ignore = true)
	InvoiceEntity fromRequest(PaymentRequest request);

	@Mapping(source = "id", target = "invoiceId")
	@Mapping(source = "totalAmount", target = "totalAmount")
	// Những trường sau không map được từ InvoiceEntity nên ignore
	@Mapping(target = "change", ignore = true)
	@Mapping(target = "customerCash", ignore = true)
	@Mapping(target = "invoiceStatus", ignore = true)
	@Mapping(target = "message", ignore = true)
	@Mapping(target = "paidById", ignore = true)
	@Mapping(target = "paidByName", ignore = true)
	@Mapping(target = "success", ignore = true)
	@Mapping(target = "tableId", ignore = true)
	PaymentResponse toDto(InvoiceEntity entity);
}
