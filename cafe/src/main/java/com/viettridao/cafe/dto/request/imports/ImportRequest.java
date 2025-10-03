package com.viettridao.cafe.dto.request.imports;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImportRequest {

    private Integer productId;

    @NotBlank(message = "Tên hàng không được bỏ trống")
    private String productName;

    @NotNull(message = "Số lượng là bắt buộc")
    @Min(value = 1, message = "Số lượng phải lớn hơn 0")
    private Integer quantity;

    @NotNull(message = "ID đơn vị tính là bắt buộc")
    private Integer unitId;

    @NotNull(message = "Đơn giá là bắt buộc")
    @DecimalMin(value = "0.1", message = "Đơn giá phải lớn hơn 0")
    private Double price;

    @NotNull(message = "Ngày nhập là bắt buộc")
    @PastOrPresent(message = "Ngày nhập không được ở tương lai")
    @DateTimeFormat(pattern = "yyyy-MM-dd") 
    private LocalDate importDate;
}
