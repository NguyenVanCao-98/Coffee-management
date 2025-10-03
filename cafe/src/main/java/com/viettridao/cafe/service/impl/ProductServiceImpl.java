package com.viettridao.cafe.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.viettridao.cafe.dto.request.product.ProductRequest;
import com.viettridao.cafe.dto.response.product.ProductResponse;
import com.viettridao.cafe.mapper.ProductMapper;
import com.viettridao.cafe.model.ImportEntity;
import com.viettridao.cafe.model.ProductEntity;
import com.viettridao.cafe.repository.ExportRepository;
import com.viettridao.cafe.repository.ImportRepository;
import com.viettridao.cafe.repository.ProductRepository;
import com.viettridao.cafe.service.ProductService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

	private final ProductRepository productRepository;
	private final ImportRepository importRepository;
	private final ExportRepository exportRepository;
	private final ProductMapper productMapper;

	@Override
	public List<ProductResponse> findAll() {
		return productRepository.findAllByIsDeletedFalse().stream().map(this::mapProductWithExtras)
				.collect(Collectors.toList());
	}

	@Override
	public Page<ProductResponse> findAllPaged(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<ProductEntity> entityPage = productRepository.findAllByIsDeletedFalse(pageable);
		List<ProductResponse> dtoList = entityPage.getContent().stream().map(this::mapProductWithExtras)
				.collect(Collectors.toList());
		return new PageImpl<>(dtoList, pageable, entityPage.getTotalElements());
	}

	@Override
	public ProductResponse findById(Integer id) {
		ProductEntity entity = productRepository.findByIdAndIsDeletedFalse(id)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));
		return mapProductWithExtras(entity);
	}

	@Override
	@Transactional
	public void save(ProductRequest request) {
		ProductEntity entity = productMapper.fromRequest(request);
		entity.setIsDeleted(false);
		productRepository.save(entity);

		ImportEntity importEntity = new ImportEntity();
		importEntity.setProduct(entity);
		importEntity.setImportDate(request.getImportDate()); // LocalDate trực tiếp
		importEntity.setPrice(request.getPrice());
		importEntity.setQuantity(request.getQuantity());
		importEntity.setTotalAmount(request.getPrice() * request.getQuantity());
		importEntity.setIsDeleted(false);
		importRepository.save(importEntity);
	}

	@Override
	@Transactional
	public void update(Integer id, ProductRequest request) {
	    ProductEntity entity = productRepository.findByIdAndIsDeletedFalse(id)
	            .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

	    // Cập nhật các trường từ request, null sẽ không ghi đè
	    productMapper.updateEntityFromRequest(request, entity);

	    // Mapper đã xử lý unit trong afterMappingRequest
	    productRepository.save(entity);

	    // Cập nhật import mới nhất
	    importRepository.findTopByProductIdOrderByImportDateDesc(id).ifPresentOrElse(importEntity -> {
	        importEntity.setImportDate(request.getImportDate());
	        importEntity.setPrice(request.getPrice());
	        importEntity.setQuantity(request.getQuantity());
	        importEntity.setTotalAmount(request.getPrice() * request.getQuantity());
	        importRepository.save(importEntity);
	    }, () -> {
	        ImportEntity importEntity = new ImportEntity();
	        importEntity.setProduct(entity);
	        importEntity.setImportDate(request.getImportDate());
	        importEntity.setPrice(request.getPrice());
	        importEntity.setQuantity(request.getQuantity());
	        importEntity.setTotalAmount(request.getPrice() * request.getQuantity());
	        importEntity.setIsDeleted(false);
	        importRepository.save(importEntity);
	    });
	}

	@Override
	@Transactional
	public void delete(Integer id) {
		ProductEntity entity = productRepository.findByIdAndIsDeletedFalse(id)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));
		entity.setIsDeleted(true);
		productRepository.save(entity);
	}

	@Override
	public int getCurrentStock(Integer productId) {
		Integer imported = importRepository.getTotalImportedQuantity(productId);
		Integer exported = exportRepository.getTotalExportedQuantity(productId);
		return (imported != null ? imported : 0) - (exported != null ? exported : 0);
	}

	
	@Override
	public ProductRequest findRequestById(Integer id) {
		ProductEntity entity = productRepository.findByIdAndIsDeletedFalse(id)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

		ProductRequest request = productMapper.toRequest(entity);

		importRepository.findTopByProductIdOrderByImportDateDesc(id).ifPresent(latestImport -> {
			request.setImportDate(latestImport.getImportDate()); // LocalDate trực tiếp
			request.setPrice(latestImport.getPrice());
		});

		request.setId(entity.getId());
		request.setProductName(entity.getProductName());
		request.setQuantity(entity.getQuantity());
		if (entity.getUnit() != null) {
			request.setUnitId(entity.getUnit().getId());
		}

		return request;
	}

	@Override
	public List<ProductResponse> search(String keyword) {
		return productRepository.findByProductNameContainingIgnoreCaseAndIsDeletedFalse(keyword).stream()
				.map(this::mapProductWithExtras).collect(Collectors.toList());
	}

	@Override
	public Page<ProductResponse> search(String keyword, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<ProductEntity> entityPage = productRepository
				.findByProductNameContainingIgnoreCaseAndIsDeletedFalse(keyword, pageable);
		List<ProductResponse> dtoList = entityPage.getContent().stream().map(this::mapProductWithExtras)
				.collect(Collectors.toList());
		return new PageImpl<>(dtoList, pageable, entityPage.getTotalElements());
	}

	@Override
	public List<ProductResponse> getAll() {
		return productRepository.findAllByIsDeletedFalse().stream().map(this::mapProductWithExtras)
				.collect(Collectors.toList());
	}

	private ProductResponse mapProductWithExtras(ProductEntity product) {
		ProductResponse dto = productMapper.toResponse(product);

		importRepository.findTopByProductIdOrderByImportDateDesc(product.getId()).ifPresent(importEntity -> {
			dto.setLatestPrice(importEntity.getPrice());
			dto.setImportDate(importEntity.getImportDate()); // LocalDate trực tiếp
			dto.setLastImportDate(importEntity.getImportDate());
			dto.setTotalAmount(product.getQuantity() * importEntity.getPrice());
		});

		exportRepository.findTopByProductIdOrderByExportDateDesc(product.getId())
				.ifPresent(exportEntity -> dto.setLastExportDate(exportEntity.getExportDate()));

		dto.setCurrentQuantity(product.getQuantity());
		return dto;
	}
}
