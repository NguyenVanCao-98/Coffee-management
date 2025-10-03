package com.viettridao.cafe.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import com.viettridao.cafe.dto.request.account.UpdateAccountRequest;
import com.viettridao.cafe.dto.response.account.AccountResponse;
import com.viettridao.cafe.model.AccountEntity;

/**
 * Mapper dùng MapStruct để chuyển đổi giữa AccountEntity và AccountResponse.
 * 
 * componentModel = "spring" để Spring quản lý bean mapper này, có
 * thể @Autowired trực tiếp.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AccountMapper {

    @Mapping(source = "employee.fullName", target = "fullName")
    @Mapping(source = "employee.address", target = "address")
    @Mapping(source = "employee.phoneNumber", target = "phoneNumber")
    @Mapping(source = "employee.position.id", target = "positionId")
    @Mapping(source = "employee.position.positionName", target = "positionName")
    @Mapping(source = "employee.position.salary", target = "salary")
    AccountResponse toDto(AccountEntity entity);

    List<AccountResponse> toDtoList(List<AccountEntity> entityList);

    @Mapping(target = "employee.fullName", source = "fullName")
    @Mapping(target = "employee.address", source = "address")
    @Mapping(target = "employee.phoneNumber", source = "phoneNumber")
    @Mapping(target = "employee.position.id", source = "positionId")
    @Mapping(target = "employee.position.positionName", source = "positionName")
    @Mapping(target = "employee.position.salary", source = "salary")
    @Mapping(target = "username", source = "username")
    @Mapping(target = "password", source = "password")
    @Mapping(target = "imageUrl", source = "imageUrl")
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "expenses", ignore = true)
    void updateEntityFromDto(UpdateAccountRequest dto, @MappingTarget AccountEntity entity);
}

