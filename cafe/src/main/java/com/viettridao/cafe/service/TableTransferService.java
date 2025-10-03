package com.viettridao.cafe.service;

/**
 * Giao diện dịch vụ cho việc quản lý chuyển bàn.
 */
public interface TableTransferService {

	/**
	 * Chuyển toàn bộ khách hàng và các món đã gọi từ một bàn sang một bàn khác.
	 *
	 * @param fromTableId Mã định danh của bàn nguồn (bàn hiện tại của khách).
	 * @param toTableId   Mã định danh của bàn đích (bàn khách sẽ chuyển đến).
	 */
	void transferTable(Integer fromTableId, Integer toTableId);
}