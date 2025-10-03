package com.viettridao.cafe.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.viettridao.cafe.model.InvoiceDetailEntity;
import com.viettridao.cafe.model.InvoiceKey;

/**
 * Repository xử lý truy vấn cho InvoiceDetailEntity — đại diện cho các mục chi
 * tiết trong một hóa đơn. Dùng khóa chính tổng hợp (InvoiceKey) gồm invoiceId
 * và productId.
 */
@Repository
public interface InvoiceItemDetailRepository extends JpaRepository<InvoiceDetailEntity, InvoiceKey> {

	/**
	 * Lấy danh sách các mục trong hóa đơn chưa bị xóa theo ID hóa đơn.
	 *
	 * @param invoiceId ID của hóa đơn
	 * @return danh sách chi tiết hóa đơn chưa bị xóa (isDeleted = false)
	 */
	List<InvoiceDetailEntity> findByInvoice_IdAndIsDeletedFalse(Integer invoiceId);

	/**
	 * Tìm chi tiết hóa đơn theo ID (InvoiceKey) nếu chưa bị xóa.
	 *
	 * @param id khóa tổng hợp gồm invoiceId và productId
	 * @return chi tiết hóa đơn nếu tồn tại và chưa bị xóa
	 */
	Optional<InvoiceDetailEntity> findByIdAndIsDeletedFalse(InvoiceKey id);

	/**
	 * Lấy tất cả chi tiết hóa đơn (không phân biệt bị xóa hay chưa) theo invoiceId.
	 *
	 * @param invoiceId ID của hóa đơn
	 * @return danh sách chi tiết hóa đơn
	 */
	List<InvoiceDetailEntity> findByInvoice_Id(Integer invoiceId);
}
