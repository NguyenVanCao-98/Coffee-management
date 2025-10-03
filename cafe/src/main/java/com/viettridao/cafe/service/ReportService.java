package com.viettridao.cafe.service;

import java.time.LocalDate;
import java.util.List;

import com.viettridao.cafe.common.ReportType;
import com.viettridao.cafe.dto.response.employee.EmployeeDailySalaryResponse;
import com.viettridao.cafe.dto.response.reportstatistics.ReportItemResponse;

/**
 * Giao diện dịch vụ cho các báo cáo và thống kê.
 */
public interface ReportService {

	/**
	 * Lấy báo cáo dựa trên khoảng thời gian và loại báo cáo cụ thể.
	 *
	 * @param fromDate Ngày bắt đầu của báo cáo.
	 * @param toDate   Ngày kết thúc của báo cáo.
	 * @param type     Loại báo cáo (ví dụ: doanh thu, chi phí).
	 * @return Danh sách các mục báo cáo.
	 */
	List<ReportItemResponse> getReport(LocalDate fromDate, LocalDate toDate, ReportType type);

	/**
	 * Lấy báo cáo tổng hợp dựa trên khoảng thời gian (không phân biệt loại).
	 *
	 * @param fromDate Ngày bắt đầu của báo cáo.
	 * @param toDate   Ngày kết thúc của báo cáo.
	 * @return Danh sách các mục báo cáo.
	 */
	List<ReportItemResponse> getReport(LocalDate fromDate, LocalDate toDate);

	/**
	 * Lấy danh sách lương hàng ngày của nhân viên trong một khoảng thời gian.
	 *
	 * @param from Ngày bắt đầu.
	 * @param to   Ngày kết thúc.
	 * @return Danh sách phản hồi lương hàng ngày của nhân viên.
	 */
	List<EmployeeDailySalaryResponse> getEmployeeDailySalaries(LocalDate from, LocalDate to);

}