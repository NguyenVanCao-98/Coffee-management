package com.viettridao.cafe.dto.request.equipment;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateEquipmentRequest {
    @NotBlank(message = "Tên thiết bị không được để trống")
    @Size(min = 5, message = "Tên thiết bị tối thiểu 5 ký tự")
    private String equipmentName;

    @NotNull(message = "Số lượng thiết bị không được để trống")
    @Min(value = 1, message = "Số lượng thiết bị phải lớn hơn 0")
    private Integer quantity;

    @NotNull(message = "Ngày mua không được để trống")
    @PastOrPresent(message = "Ngày mua không được lớn hơn ngày hiện tại")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate purchaseDate;

    @NotNull(message = "Giá mua không được để trống")
    @DecimalMin(value = "0.0", inclusive = false, message = "Giá mua phải lớn hơn 0")
    private Double purchasePrice;
}
