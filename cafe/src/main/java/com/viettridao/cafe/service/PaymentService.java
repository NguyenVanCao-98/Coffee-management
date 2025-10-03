package com.viettridao.cafe.service;

import com.viettridao.cafe.dto.request.Pay.PaymentRequest;
import com.viettridao.cafe.dto.response.Pay.PaymentResponse;

/**
 * Interface định nghĩa các phương thức liên quan đến xử lý thanh toán.
 */
public interface PaymentService {

	/**
	 * Xử lý thanh toán dựa trên thông tin được cung cấp trong yêu cầu.
	 *
	 * @param request đối tượng chứa thông tin chi tiết về thanh toán (ví dụ: số
	 *                tiền, phương thức thanh toán, hóa đơn liên quan,...)
	 * @return đối tượng chứa kết quả sau khi xử lý thanh toán (ví dụ: trạng thái
	 *         thanh toán, mã giao dịch,...)
	 */
	PaymentResponse processPayment(PaymentRequest request);
}
