package com.viettridao.cafe.service.impl;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.viettridao.cafe.dto.request.promotion.CreatePromotionRequest;
import com.viettridao.cafe.dto.request.promotion.UpdatePromotionRequest;
import com.viettridao.cafe.dto.response.promotion.PromotionPageResponse;
import com.viettridao.cafe.mapper.PromotionMapper;
import com.viettridao.cafe.model.PromotionEntity;
import com.viettridao.cafe.repository.PromotionRepository;
import com.viettridao.cafe.service.PromotionService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PromotionServiceImpl implements PromotionService {

	private final PromotionRepository promotionRepository;
	private final PromotionMapper promotionMapper;

	@Override
	public PromotionPageResponse getAllPromotions(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<PromotionEntity> promotions = promotionRepository.getAllByPromotions(pageable);

		PromotionPageResponse promotionPageResponse = new PromotionPageResponse();
		promotionPageResponse.setPageNumber(promotions.getNumber());
		promotionPageResponse.setPageSize(promotions.getSize());
		promotionPageResponse.setTotalElements(promotions.getTotalElements());
		promotionPageResponse.setTotalPages(promotions.getTotalPages());
		promotionPageResponse.setPromotions(promotionMapper.toDtoList(promotions.getContent()));

		return promotionPageResponse;
	}

	@Transactional
	@Override
	public PromotionEntity createPromotion(CreatePromotionRequest request) {
		validatePromotionDate(request.getStartDate(), request.getEndDate());

		PromotionEntity promotion = new PromotionEntity();
		promotion.setPromotionName(request.getPromotionName());
		promotion.setStartDate(request.getStartDate());
		promotion.setEndDate(request.getEndDate());
		promotion.setDiscountValue(request.getDiscountValue());
		promotion.setIsDeleted(false);

		return promotionRepository.save(promotion);
	}

	@Transactional
	@Override
	public void updatePromotion(UpdatePromotionRequest request) {
		validatePromotionDate(request.getStartDate(), request.getEndDate());

		PromotionEntity promotion = getPromotionById(request.getId());
		promotion.setPromotionName(request.getPromotionName());
		promotion.setStartDate(request.getStartDate());
		promotion.setEndDate(request.getEndDate());
		promotion.setDiscountValue(request.getDiscountValue());

		promotionRepository.save(promotion);
	}

	@Override
	public PromotionEntity getPromotionById(Integer id) {
		return promotionRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Không tìm thấy khuyến mãi có id=" + id));
	}

	@Transactional
	@Override
	public void deletePromotion(Integer id) {
		PromotionEntity promotion = getPromotionById(id);
		promotion.setIsDeleted(true);
		promotionRepository.save(promotion);
	}

	private void validatePromotionDate(LocalDate startDate, LocalDate endDate) {
		if (endDate.isBefore(startDate)) {
			throw new IllegalArgumentException("Ngày kết thúc phải sau hoặc bằng ngày bắt đầu");
		}
	}
}
