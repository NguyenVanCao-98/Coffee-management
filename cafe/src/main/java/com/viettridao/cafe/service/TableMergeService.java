package com.viettridao.cafe.service;

import java.util.List;

/**
 * Giao diện dịch vụ cho việc quản lý gộp bàn.
 */
public interface TableMergeService {

	/**
	 * Gộp nhiều bàn nhỏ vào một bàn lớn hơn hoặc một bàn chính.
	 *
	 * @param targetTableId  Mã định danh của bàn đích (bàn sẽ nhận các khách từ bàn
	 *                       nguồn).
	 * @param sourceTableIds Danh sách các mã định danh của các bàn nguồn (các bàn
	 *                       sẽ được gộp vào bàn đích).
	 * @param customerName   Tên khách hàng đại diện cho nhóm khách hàng sau khi gộp
	 *                       bàn.
	 * @param customerPhone  Số điện thoại của khách hàng đại diện cho nhóm khách
	 *                       hàng sau khi gộp bàn.
	 */
	void mergeTables(Integer targetTableId, List<Integer> sourceTableIds, String customerName, String customerPhone);

}