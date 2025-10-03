package com.viettridao.cafe.service;

import java.util.List;

import com.viettridao.cafe.dto.request.tables.MenuItemSplitRequest;

/**
 * Giao diện dịch vụ cho việc quản lý tách bàn.
 */
public interface TableSplitService {

	/**
	 * Tách các món hàng từ một bàn hiện có sang một bàn khác (có thể là bàn mới
	 * hoặc bàn đã tồn tại).
	 *
	 * @param fromTableId   Mã định danh của bàn nguồn (bàn cần tách món).
	 * @param toTableId     Mã định danh của bàn đích (bàn sẽ nhận các món đã tách).
	 * @param itemsToSplit  Danh sách các yêu cầu tách món, bao gồm ID món và số
	 *                      lượng cần tách.
	 * @param customerName  Tên khách hàng đại diện cho bàn đích sau khi tách.
	 * @param customerPhone Số điện thoại của khách hàng đại diện cho bàn đích sau
	 *                      khi tách.
	 */
	void splitTable(Integer fromTableId, Integer toTableId, List<MenuItemSplitRequest> itemsToSplit,
			String customerName, String customerPhone);

}