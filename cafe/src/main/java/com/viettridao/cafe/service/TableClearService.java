package com.viettridao.cafe.service;

/**
 * Giao diện dịch vụ cho việc quản lý dọn dẹp bàn.
 */
public interface TableClearService {

	/**
	 * Thực hiện dọn dẹp một bàn cụ thể sau khi khách rời đi hoặc khi không còn sử
	 * dụng.
	 *
	 * @param tableId Mã định danh của bàn cần dọn.
	 */
	void clearTable(Integer tableId);
}