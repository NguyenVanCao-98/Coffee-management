package com.viettridao.cafe.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.viettridao.cafe.dto.request.export.ExportRequest;
import com.viettridao.cafe.dto.response.exports.ExportResponse;

/**
 * Interface định nghĩa các phương thức phục vụ quản lý xuất hàng trong hệ
 * thống.
 */
public interface ExportService {

	/**
	 * Tạo mới một bản ghi xuất hàng dựa trên dữ liệu từ request.
	 *
	 * @param request thông tin chi tiết về lệnh xuất hàng
	 * @return ExportResponse chứa thông tin xuất hàng vừa tạo
	 */
	ExportResponse createExport(ExportRequest request);

	/**
	 * Thực hiện xuất sản phẩm theo yêu cầu.
	 *
	 * @param request dữ liệu yêu cầu xuất sản phẩm
	 */
	void exportProduct(ExportRequest request);

	/**
	 * Lấy danh sách tất cả các bản ghi xuất hàng.
	 *
	 * @return danh sách ExportResponse đại diện cho các bản ghi xuất hàng
	 */
	List<ExportResponse> getAll();

	/**
	 * Lấy danh sách các bản ghi xuất hàng theo ID sản phẩm cụ thể.
	 *
	 * @param productId ID của sản phẩm cần lấy lịch sử xuất
	 * @return danh sách ExportResponse liên quan đến sản phẩm đó
	 */
	List<ExportResponse> getExportsByProduct(Integer productId);

	/**
	 * Lấy danh sách các bản ghi xuất hàng theo ID sản phẩm, có phân trang.
	 *
	 * @param productId ID của sản phẩm cần lấy lịch sử xuất
	 * @param page      số trang bắt đầu từ 0
	 * @param size      số lượng bản ghi trên mỗi trang
	 * @return đối tượng Page chứa các ExportResponse của trang tương ứng
	 */
	Page<ExportResponse> getExportsByProductId(Integer productId, int page, int size);
}
