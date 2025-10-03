package com.viettridao.cafe.mapper;

import java.time.LocalDateTime;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.viettridao.cafe.dto.response.expenses.BudgetViewResponse;
import com.viettridao.cafe.model.InvoiceEntity;

/**
 * Mapper sử dụng MapStruct để chuyển đổi từ InvoiceEntity sang
 * BudgetViewResponse.
 * 
 * componentModel = "spring" để Spring quản lý bean mapper này, có
 * thể @Autowired trực tiếp.
 */
@Mapper(componentModel = "spring")
public interface InvoiceMapper {

	/**
	 * Chuyển đổi từ InvoiceEntity sang BudgetViewResponse. Ánh xạ các trường: -
	 * date được lấy từ createdAt của entity, chuyển từ LocalDateTime sang LocalDate
	 * bằng phương thức convertToDate. - income được lấy từ totalAmount. - expense
	 * được gán hằng số 0.0.
	 * 
	 * @param entity đối tượng InvoiceEntity nguồn
	 * @return đối tượng BudgetViewResponse đích
	 */
	@Mapping(target = "date", expression = "java(convertToDate(entity.getCreatedAt()))")
	@Mapping(target = "income", source = "totalAmount")
	@Mapping(target = "expense", constant = "0.0")
	BudgetViewResponse toBudgetResponse(InvoiceEntity entity);

	/**
	 * Phương thức tiện ích chuyển đổi LocalDateTime sang LocalDate. Nếu giá trị
	 * datetime là null thì trả về null.
	 * 
	 * @param datetime đối tượng LocalDateTime cần chuyển đổi
	 * @return LocalDate tương ứng hoặc null
	 */
	default java.time.LocalDate convertToDate(LocalDateTime datetime) {
		return datetime != null ? datetime.toLocalDate() : null;
	}
}
