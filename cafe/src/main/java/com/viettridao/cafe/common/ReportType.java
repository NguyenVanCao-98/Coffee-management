package com.viettridao.cafe.common;

/**
 * Enum đại diện cho các loại báo cáo có thể được sử dụng trong hệ thống quản lý
 * quán cafe.
 */
public enum ReportType {
	ALL, // Tất cả loại báo cáo
	IMPORT_EXPORT, // Báo cáo nhập - xuất hàng
	IMPORT, // Báo cáo nhập hàng
	EXPORT, // Báo cáo xuất hàng
	SALE, // Báo cáo doanh thu bán hàng
	SALARY, // Báo cáo lương nhân viên
	EMPLOYEE_INFO, // Báo cáo thông tin nhân sự
	OTHER_EXPENSE // Báo cáo các chi phí khác
}
