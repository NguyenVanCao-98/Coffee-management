package com.viettridao.cafe.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.viettridao.cafe.dto.response.tables.TableMenuItemResponse;
import com.viettridao.cafe.dto.response.tables.TableResponse;
import com.viettridao.cafe.model.InvoiceEntity;
import com.viettridao.cafe.model.ReservationEntity;

/**
 * Giao diện dịch vụ cho việc quản lý bàn và các thông tin liên quan.
 */
public interface TableService {

	/**
	 * Lấy tất cả các bàn có phân trang.
	 *
	 * @param page Số trang hiện tại (bắt đầu từ 0).
	 * @param size Kích thước của mỗi trang.
	 * @return Một đối tượng Page chứa danh sách các TableResponse.
	 */
	Page<TableResponse> getAllTables(int page, int size);

	/**
	 * Lấy danh sách các món ăn/thức uống (menu items) hiện có trên một bàn cụ thể.
	 *
	 * @param tableId Mã định danh của bàn.
	 * @return Danh sách các TableMenuItemResponse.
	 */
	List<TableMenuItemResponse> getTableMenuItems(Integer tableId);

	/**
	 * Lấy hoặc tạo mới một ID hóa đơn cho một bàn cụ thể. Nếu bàn đã có hóa đơn
	 * chưa thanh toán, sẽ trả về ID của hóa đơn đó. Nếu không, sẽ tạo một hóa đơn
	 * mới và trả về ID của hóa đơn đó.
	 *
	 * @param tableId Mã định danh của bàn.
	 * @return ID của hóa đơn.
	 */
	Integer getOrCreateInvoiceIdByTableId(Integer tableId);

	/**
	 * Lấy thông tin đặt bàn gần nhất cho một bàn cụ thể.
	 *
	 * @param tableId Mã định danh của bàn.
	 * @return Đối tượng ReservationEntity của đặt bàn gần nhất, hoặc null nếu không
	 *         có.
	 */
	ReservationEntity getLatestReservationByTableId(Integer tableId);

	/**
	 * Lấy hóa đơn chưa thanh toán gần nhất cho một bàn cụ thể.
	 *
	 * @param tableId Mã định danh của bàn.
	 * @return Đối tượng InvoiceEntity của hóa đơn chưa thanh toán gần nhất, hoặc
	 *         null nếu không có.
	 */
	InvoiceEntity getLatestUnpaidInvoiceByTableId(Integer tableId);

	/**
	 * Lấy hóa đơn gần nhất (bao gồm cả đã thanh toán và chưa thanh toán) cho một
	 * bàn cụ thể.
	 *
	 * @param tableId Mã định danh của bàn.
	 * @return Đối tượng InvoiceEntity của hóa đơn gần nhất, hoặc null nếu không có.
	 */
	InvoiceEntity getLatestInvoiceByTableId(Integer tableId);
	
	
	TableResponse getTableById(Integer tableId);

}
