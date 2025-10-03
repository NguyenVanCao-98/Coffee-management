package com.viettridao.cafe.mapper;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.viettridao.cafe.dto.response.reportstatistics.ReportItemResponse;

/**
 * ReportMapper chịu trách nhiệm ánh xạ dữ liệu từ các tham số đơn lẻ (ngày,
 * doanh thu, chi phí) thành một đối tượng {@link ReportItemResponse} dùng cho
 * thống kê báo cáo.
 * 
 * Đây là một Mapper thủ công, không sử dụng MapStruct vì chỉ xử lý logic đơn
 * giản.
 */
@Component
public class ReportMapper {

	/**
	 * Tạo một đối tượng {@link ReportItemResponse} từ dữ liệu đầu vào: - Ngày thống
	 * kê - Doanh thu (revenue) - Chi phí (expense)
	 * 
	 * Nếu doanh thu hoặc chi phí bị null thì mặc định gán giá trị 0.0 để tránh lỗi
	 * NullPointerException.
	 *
	 * @param date    Ngày thống kê
	 * @param revenue Tổng doanh thu trong ngày (có thể null)
	 * @param expense Tổng chi phí trong ngày (có thể null)
	 * @return Đối tượng ReportItemResponse đã được chuẩn hóa
	 */
	public ReportItemResponse toReportItem(LocalDate date, Double revenue, Double expense) {
		return new ReportItemResponse(date, revenue != null ? revenue : 0.0, expense != null ? expense : 0.0);
	}
}
