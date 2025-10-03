package com.viettridao.cafe.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.viettridao.cafe.model.InvoiceDetailEntity;
import com.viettridao.cafe.model.InvoiceKey;

/**
 * Repository cho entity InvoiceDetailEntity — lưu chi tiết hóa đơn gồm sản
 * phẩm, số lượng, đơn giá,... Sử dụng khóa chính dạng phức hợp (InvoiceKey gồm
 * invoiceId + productId).
 */
@Repository
public interface InvoiceDetailRepository extends JpaRepository<InvoiceDetailEntity, InvoiceKey> {

	/**
	 * Lấy danh sách chi tiết hóa đơn theo ID của hóa đơn.
	 *
	 * @param invoiceId ID của hóa đơn
	 * @return danh sách chi tiết trong hóa đơn đó
	 */
	List<InvoiceDetailEntity> findByInvoice_Id(Integer invoiceId);
}
