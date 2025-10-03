package com.viettridao.cafe.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.viettridao.cafe.dto.request.expenses.ExpenseRequest;
import com.viettridao.cafe.dto.response.expenses.BudgetViewResponse;
import com.viettridao.cafe.model.ExpenseEntity;

/**
 * Mapper sử dụng MapStruct để chuyển đổi giữa ExpenseEntity, DTO request và
 * response.
 * 
 * componentModel = "spring" để Spring quản lý bean mapper này, có
 * thể @Autowired trực tiếp.
 */
@Mapper(componentModel = "spring")
public interface ExpenseMapper {

    @Mapping(target = "date", source = "expenseDate")
    @Mapping(target = "expense", source = "amount")
    @Mapping(target = "income", constant = "0.0")
    BudgetViewResponse toDto(ExpenseEntity entity);

    @Mappings({
        @Mapping(target = "id", ignore = true),               // id do DB sinh tự động
        @Mapping(target = "isDeleted", constant = "false"),  // mặc định chưa xoá
        @Mapping(target = "account", ignore = true)           // account phải set thủ công ở service nếu cần
    })
    ExpenseEntity fromRequest(ExpenseRequest request);
}
