package com.viettridao.cafe.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.viettridao.cafe.dto.request.imports.ImportRequest;
import com.viettridao.cafe.dto.response.imports.ImportResponse;
import com.viettridao.cafe.mapper.ImportMapper;
import com.viettridao.cafe.model.ImportEntity;
import com.viettridao.cafe.model.ProductEntity;
import com.viettridao.cafe.model.UnitEntity;
import com.viettridao.cafe.repository.ImportRepository;
import com.viettridao.cafe.repository.ProductRepository;
import com.viettridao.cafe.repository.UnitRepository;
import com.viettridao.cafe.service.ImportService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

/**
 * Triển khai các chức năng nhập kho sản phẩm.
 */
@Service
@RequiredArgsConstructor
public class ImportServiceImpl implements ImportService {

	private final ImportRepository importRepository;
	private final ProductRepository productRepository;
	private final UnitRepository unitRepository;
	private final ImportMapper importMapper;

	/**
	 * Xử lý tạo đơn nhập kho mới. Nếu sản phẩm chưa tồn tại sẽ tự động tạo mới.
	 * Đồng thời cập nhật lại số lượng tồn kho của sản phẩm.
	 *
	 * @param request ImportRequest - dữ liệu đơn nhập từ client
	 * @return ImportResponse - thông tin đơn nhập mới nhất
	 */
	@Override
	@Transactional
	public ImportResponse createImport(ImportRequest request) {
		// ✅ Kiểm tra số lượng nhập phải lớn hơn 0
		if (request.getQuantity() == null || request.getQuantity() <= 0) {
			throw new IllegalArgumentException("Số lượng nhập phải lớn hơn 0.");
		}

		// ✅ Kiểm tra giá nhập không được âm
		if (request.getPrice() == null || request.getPrice() < 0) {
			throw new IllegalArgumentException("Đơn giá nhập không được để trống hoặc âm.");
		}

		ProductEntity product;

		// ✅ Trường hợp sản phẩm đã tồn tại (theo productId)
		if (request.getProductId() != null) {
			product = productRepository.findById(request.getProductId()).orElseThrow(
					() -> new RuntimeException("Không tìm thấy sản phẩm với ID: " + request.getProductId()));
		}
		// ✅ Nếu sản phẩm chưa tồn tại, thì kiểm tra theo tên. Nếu cũng không có thì tạo
		// mới.
		else {
			product = productRepository.findByProductNameIgnoreCase(request.getProductName()).orElseGet(() -> {
				// Tìm đơn vị tính theo unitId
				UnitEntity unit = unitRepository.findByIdAndIsDeletedFalse(request.getUnitId()).orElseThrow(
						() -> new RuntimeException("Không tìm thấy đơn vị tính với ID: " + request.getUnitId()));

				// Tạo sản phẩm mới
				ProductEntity newProduct = new ProductEntity();
				newProduct.setProductName(request.getProductName());
				newProduct.setUnit(unit);
				newProduct.setQuantity(0); // ban đầu chưa có tồn kho
				newProduct.setIsDeleted(false);

				return productRepository.save(newProduct);
			});
		}

		// ✅ Tạo đơn nhập từ dữ liệu request
		ImportEntity entity = importMapper.fromRequest(request);
		entity.setProduct(product);
		entity.setPrice(request.getPrice());
		entity.setTotalAmount(request.getQuantity() * request.getPrice()); // tính tổng tiền nhập
		entity.setIsDeleted(false);

		// ✅ Lưu đơn nhập
		importRepository.save(entity);

		// ✅ Cập nhật số lượng tồn kho cho sản phẩm
		int updatedQuantity = product.getQuantity() + request.getQuantity();
		product.setQuantity(updatedQuantity);
		productRepository.save(product);

		// ✅ Lấy đơn nhập mới nhất và trả về
		return importMapper.toDto(importRepository.findTopByProductIdOrderByImportDateDesc(product.getId()).get());
	}

	/**
	 * Lấy toàn bộ đơn nhập chưa bị xóa. Sử dụng JOIN FETCH để tránh lỗi
	 * lazy-loading với product.
	 *
	 * @return List<ImportResponse> - danh sách đơn nhập
	 */
	@Override
	public List<ImportResponse> getAll() {
		return importRepository.findAllWithProduct().stream().map(importMapper::toDto).toList();
	}

	/**
	 * Lấy danh sách đơn nhập theo sản phẩm.
	 *
	 * @param productId ID của sản phẩm
	 * @return List<ImportResponse> - danh sách đơn nhập
	 */
	@Override
	public List<ImportResponse> getImportsByProduct(Integer productId) {
		return importRepository.findByProductIdAndIsDeletedFalse(productId).stream().map(importMapper::toDto).toList();
	}

	/**
	 * Lấy đơn nhập theo sản phẩm với phân trang.
	 *
	 * @param productId ID của sản phẩm
	 * @param page      số trang hiện tại
	 * @param size      số bản ghi mỗi trang
	 * @return Page<ImportResponse> - dữ liệu phân trang
	 */
	@Override
	public Page<ImportResponse> getImportsByProductId(Integer productId, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		return importRepository.findByProductIdAndIsDeletedFalse(productId, pageable).map(importMapper::toDto);
	}
}
