package com.viettridao.cafe.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.viettridao.cafe.dto.response.tables.TableResponse;
import com.viettridao.cafe.model.TableEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TableMapper {

    @Mapping(target = "invoiceId", expression = "java(getInvoiceIdFromReservations(entity))")
    TableResponse toDto(TableEntity entity);

    List<TableResponse> toDtoList(List<TableEntity> entities);

    // Hàm helper để lấy invoiceId
    default Integer getInvoiceIdFromReservations(TableEntity entity) {
        if (entity.getReservations() == null || entity.getReservations().isEmpty()) {
            return null;
        }
        // Ở đây mình lấy hóa đơn đầu tiên có trong reservations
        return entity.getReservations().get(0).getInvoice() != null
                ? entity.getReservations().get(0).getInvoice().getId()
                : null;
    }
}

