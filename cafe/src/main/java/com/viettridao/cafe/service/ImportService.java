package com.viettridao.cafe.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.viettridao.cafe.dto.request.imports.ImportRequest;
import com.viettridao.cafe.dto.response.imports.ImportResponse;

/**
 * Interface định nghĩa các phương thức phục vụ quản lý nhập hàng trong hệ
 * thống.
 */
public interface ImportService {

	/**
	 * Tạo mới một bản ghi nhập hàng dựa trên dữ liệu từ request.
	 *
	 * @param request thông tin chi tiết về lệnh nhập hàng
	 * @return ImportResponse chứa thông tin nhập hàng vừa tạo
	 */
	ImportResponse createImport(ImportRequest request);

	/**
	 * Lấy danh sách tất cả các bản ghi nhập hàng.
	 *
	 * @return danh sách ImportResponse đại diện cho các bản ghi nhập hàng
	 */
	List<ImportResponse> getAll();

	/**
	 * Lấy danh sách các bản ghi nhập hàng theo ID sản phẩm cụ thể.
	 *
	 * @param productId ID của sản phẩm cần lấy lịch sử nhập
	 * @return danh sách ImportResponse liên quan đến sản phẩm đó
	 */
	List<ImportResponse> getImportsByProduct(Integer productId);

	/**
	 * Lấy danh sách các bản ghi nhập hàng theo ID sản phẩm, có phân trang.
	 *
	 * @param productId ID của sản phẩm cần lấy lịch sử nhập
	 * @param page      số trang bắt đầu từ 0
	 * @param size      số lượng bản ghi trên mỗi trang
	 * @return đối tượng Page chứa các ImportResponse của trang tương ứng
	 */
	Page<ImportResponse> getImportsByProductId(Integer productId, int page, int size);
}
