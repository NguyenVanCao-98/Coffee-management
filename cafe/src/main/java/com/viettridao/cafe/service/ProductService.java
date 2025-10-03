package com.viettridao.cafe.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.viettridao.cafe.dto.request.product.ProductRequest;
import com.viettridao.cafe.dto.response.product.ProductResponse;

/**
 * Interface định nghĩa các phương thức thao tác liên quan đến sản phẩm.
 */
public interface ProductService {

	/**
	 * Lấy danh sách tất cả sản phẩm.
	 *
	 * @return danh sách các sản phẩm dưới dạng ProductResponse
	 */
	List<ProductResponse> findAll();

	/**
	 * Lấy danh sách sản phẩm theo phân trang.
	 *
	 * @param page số trang bắt đầu từ 0
	 * @param size số lượng sản phẩm trên mỗi trang
	 * @return trang dữ liệu sản phẩm dưới dạng Page<ProductResponse>
	 */
	Page<ProductResponse> findAllPaged(int page, int size);

	/**
	 * Tìm sản phẩm theo ID.
	 *
	 * @param id ID sản phẩm cần tìm
	 * @return đối tượng ProductResponse tương ứng với ID
	 */
	ProductResponse findById(Integer id);

	/**
	 * Thêm mới sản phẩm dựa trên thông tin trong request.
	 *
	 * @param request thông tin sản phẩm cần lưu
	 */
	void save(ProductRequest request);

	/**
	 * Cập nhật thông tin sản phẩm theo ID.
	 *
	 * @param id      ID sản phẩm cần cập nhật
	 * @param request thông tin mới để cập nhật cho sản phẩm
	 */
	void update(Integer id, ProductRequest request);

	/**
	 * Xóa sản phẩm theo ID.
	 *
	 * @param id ID sản phẩm cần xóa
	 */
	void delete(Integer id);

	/**
	 * Lấy số lượng tồn kho hiện tại của sản phẩm.
	 *
	 * @param productId ID sản phẩm cần kiểm tra tồn kho
	 * @return số lượng tồn kho hiện tại
	 */
	int getCurrentStock(Integer productId);

	/**
	 * Tìm kiếm sản phẩm theo từ khóa không phân trang.
	 *
	 * @param keyword từ khóa tìm kiếm
	 * @return danh sách sản phẩm thỏa mãn điều kiện tìm kiếm
	 */
	List<ProductResponse> search(String keyword);

	/**
	 * Tìm kiếm sản phẩm theo từ khóa với phân trang.
	 *
	 * @param keyword từ khóa tìm kiếm
	 * @param page    số trang bắt đầu từ 0
	 * @param size    số lượng sản phẩm trên mỗi trang
	 * @return trang dữ liệu sản phẩm thỏa mãn điều kiện tìm kiếm
	 */
	Page<ProductResponse> search(String keyword, int page, int size);
	
	List<ProductResponse> getAll();
	
	ProductRequest findRequestById(Integer id);

}
