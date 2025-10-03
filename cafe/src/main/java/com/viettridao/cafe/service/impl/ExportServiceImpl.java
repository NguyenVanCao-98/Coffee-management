package com.viettridao.cafe.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.viettridao.cafe.dto.request.export.ExportRequest;
import com.viettridao.cafe.dto.response.exports.ExportResponse;
import com.viettridao.cafe.mapper.ExportMapper;
import com.viettridao.cafe.model.ExportEntity;
import com.viettridao.cafe.model.ImportEntity;
import com.viettridao.cafe.model.ProductEntity;
import com.viettridao.cafe.repository.ExportRepository;
import com.viettridao.cafe.repository.ImportRepository;
import com.viettridao.cafe.repository.ProductRepository;
import com.viettridao.cafe.service.ExportService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

/**
 * Triển khai ExportService xử lý các nghiệp vụ liên quan đến xuất sản phẩm.
 */
@Service
@RequiredArgsConstructor
public class ExportServiceImpl implements ExportService {

	private final ExportRepository exportRepository;

	private final ProductRepository productRepository;

	private final ImportRepository importRepository;

	private final ExportMapper exportMapper;

	/**
	 * Tạo đơn xuất hàng và trả về thông tin đơn xuất gần nhất.
	 *
	 * @param request ExportRequest - dữ liệu xuất hàng từ người dùng
	 * @return ExportResponse - thông tin đơn xuất mới nhất
	 */
	@Override
	@Transactional
	public ExportResponse createExport(ExportRequest request) {
		// Gọi xử lý xuất hàng
		exportProduct(request);

		// Lấy đơn xuất gần nhất của sản phẩm vừa xuất
		Optional<ExportEntity> latestExport = exportRepository.findTopByProductIdOrderByExportDateDesc(request.getProductId()).stream()
				.findFirst();

		// Nếu không có đơn xuất thì ném lỗi
		if (latestExport.isEmpty()) {
			throw new RuntimeException("Không tìm thấy đơn xuất gần nhất");
		}

		// Chuyển sang DTO và trả về
		return exportMapper.toDto(latestExport.get());
	}

	/**
	 * Xử lý xuất hàng: tạo đơn xuất và cập nhật tồn kho.
	 *
	 * @param request ExportRequest - thông tin đơn xuất
	 */
	@Override
	@Transactional
	public void exportProduct(ExportRequest request) {
		// Lấy sản phẩm từ CSDL theo ID
		ProductEntity product = productRepository.findByIdAndIsDeletedFalse(request.getProductId())
				.orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

		// Kiểm tra số lượng tồn kho
		if (product.getQuantity() < request.getQuantity()) {
			throw new RuntimeException("Số lượng xuất vượt quá tồn kho");
		}

		// Lấy đơn nhập gần nhất để tính giá xuất
		ImportEntity latestImport = importRepository.findTopByProductIdOrderByImportDateDesc(product.getId()).stream().findFirst()
				.orElseThrow(() -> new RuntimeException("Không tìm thấy đơn nhập gần nhất cho sản phẩm"));

		Double unitPrice = latestImport.getPrice(); // giá nhập làm giá xuất

		// Tạo entity từ request
		ExportEntity entity = exportMapper.fromRequest(request);
		entity.setIsDeleted(false); // Mặc định đơn xuất chưa bị xóa
		entity.setProduct(product); // Gán quan hệ với sản phẩm
		entity.setTotalExportAmount(request.getQuantity() * unitPrice); // Tính tổng tiền xuất

		// Nếu có yêu cầu thêm nhân viên xuất kho thì có thể set ở đây
		// entity.setEmployee(...);

		// Lưu đơn xuất
		exportRepository.save(entity);

		// Cập nhật lại tồn kho sau khi xuất
		product.setQuantity(product.getQuantity() - request.getQuantity());
		productRepository.save(product);
	}

	/**
	 * Lấy toàn bộ đơn xuất chưa bị xóa.
	 *
	 * @return List<ExportResponse> - danh sách đơn xuất
	 */
	@Override
	public List<ExportResponse> getAll() {
		return exportRepository.findByIsDeletedFalse().stream().map(exportMapper::toDto).toList();
	}

	/**
	 * Lấy danh sách đơn xuất theo sản phẩm.
	 *
	 * @param productId Integer - ID sản phẩm
	 * @return List<ExportResponse> - danh sách đơn xuất
	 */
	@Override
	public List<ExportResponse> getExportsByProduct(Integer productId) {
		return exportRepository.findByProductIdAndIsDeletedFalse(productId).stream().map(exportMapper::toDto).toList();
	}

	/**
	 * Lấy danh sách đơn xuất theo sản phẩm có phân trang.
	 *
	 * @param productId Integer - ID sản phẩm
	 * @param page      int - trang hiện tại
	 * @param size      int - số lượng phần tử mỗi trang
	 * @return Page<ExportResponse> - trang dữ liệu đơn xuất
	 */
	@Override
	public Page<ExportResponse> getExportsByProductId(Integer productId, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);

		// Trả về danh sách đơn xuất phân trang theo productId
		return exportRepository.findByProductIdAndIsDeletedFalse(productId, pageable).map(exportMapper::toDto);
	}
}
