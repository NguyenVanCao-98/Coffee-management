package com.viettridao.cafe.service;

import com.viettridao.cafe.dto.request.tables.TableBookingRequest;
import com.viettridao.cafe.dto.response.tables.TableBookingResponse;

/**
 * Giao diện dịch vụ cho việc quản lý đặt bàn.
 */
public interface ReservationService {

	/**
	 * Thực hiện đặt bàn dựa trên yêu cầu.
	 *
	 * @param request Đối tượng yêu cầu đặt bàn chứa thông tin chi tiết.
	 * @return Đối tượng phản hồi đặt bàn chứa kết quả của việc đặt bàn.
	 */
	TableBookingResponse bookTable(TableBookingRequest request);
}