package com.viettridao.cafe.service;

import com.viettridao.cafe.dto.request.promotion.CreatePromotionRequest;
import com.viettridao.cafe.dto.request.promotion.UpdatePromotionRequest;
import com.viettridao.cafe.dto.response.promotion.PromotionPageResponse;
import com.viettridao.cafe.model.PromotionEntity;

/**
 * Giao diện dịch vụ để quản lý các chương trình khuyến mãi. Định nghĩa hợp đồng
 * cho các hoạt động liên quan đến khuyến mãi.
 */
public interface PromotionService {

	/**
	 * Lấy danh sách khuyến mãi được phân trang.
	 *
	 * @param page Số trang cần lấy (bắt đầu từ 0).
	 * @param size Số lượng khuyến mãi trên mỗi trang.
	 * @return Một đối tượng {@link PromotionPageResponse} chứa danh sách khuyến mãi
	 *         được phân trang và tổng số phần tử.
	 */
	PromotionPageResponse getAllPromotions(int page, int size);

	/**
	 * Tạo một chương trình khuyến mãi mới dựa trên dữ liệu yêu cầu được cung cấp.
	 *
	 * @param request Đối tượng {@link CreatePromotionRequest} chứa thông tin chi
	 *                tiết cho chương trình khuyến mãi mới.
	 * @return Đối tượng {@link PromotionEntity} đã được tạo.
	 */
	PromotionEntity createPromotion(CreatePromotionRequest request);

	/**
	 * Cập nhật một chương trình khuyến mãi hiện có với dữ liệu yêu cầu được cung
	 * cấp.
	 *
	 * @param request Đối tượng {@link UpdatePromotionRequest} chứa thông tin chi
	 *                tiết cập nhật cho chương trình khuyến mãi.
	 */
	void updatePromotion(UpdatePromotionRequest request);

	/**
	 * Lấy một chương trình khuyến mãi theo định danh duy nhất của nó.
	 *
	 * @param id ID của chương trình khuyến mãi cần lấy.
	 * @return Đối tượng {@link PromotionEntity} tương ứng với ID đã cho, hoặc
	 *         {@code null} nếu không tìm thấy.
	 */
	PromotionEntity getPromotionById(Integer id);

	/**
	 * Xóa một chương trình khuyến mãi theo định danh duy nhất của nó.
	 *
	 * @param id ID của chương trình khuyến mãi cần xóa.
	 */
	void deletePromotion(Integer id);
}