package com.viettridao.cafe.dto.request.promotion;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UpdatePromotionRequest {
    @NotNull(message = "Id khuyến mãi không được để trống")
    @Min(value = 1, message = "Id khuyến mãi phải lớn hơn 0")
    private Integer id;

    @NotBlank(message = "Tên khuyến mãi không được để trống")
    @Size(min = 5, message = "Tên khuyến mãi tối thiểu 5 ký tự")
    private String promotionName;

    @NotNull(message = "Ngày bắt đầu không được để trống")
    @FutureOrPresent(message = "Ngày bắt đầu không được ở quá khứ")
    private LocalDate startDate;

    @NotNull(message = "Ngày kết thúc không được để trống")
    @FutureOrPresent(message = "Ngày kết thúc không được ở quá khứ")
    private LocalDate endDate;

    @NotNull(message = "Phần trăm giảm giá không được để trống")
    @DecimalMin(value = "0.0", inclusive = false, message = "Phần trăm giảm giá phải lớn hơn 0")
    @DecimalMax(value = "100.0", message = "Phần trăm giảm giá phải nhỏ hơn hoặc bằng 100")
    private Double discountValue;

}
