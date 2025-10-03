package com.viettridao.cafe.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.viettridao.cafe.model.ExportEntity;

/**
 * Repository xử lý các thao tác truy vấn liên quan đến việc xuất hàng
 * (ExportEntity).
 */
@Repository
public interface ExportRepository extends JpaRepository<ExportEntity, Integer> {

    List<ExportEntity> findByIsDeletedFalse();

    List<ExportEntity> findByProductIdAndIsDeletedFalse(Integer productId);

    Page<ExportEntity> findByProductIdAndIsDeletedFalse(Integer productId, Pageable pageable);

    Page<ExportEntity> findByProduct_ProductNameContainingIgnoreCaseAndIsDeletedFalse(
            String keyword, Pageable pageable);

    List<ExportEntity> findByExportDateBetweenAndIsDeletedFalse(LocalDate start, LocalDate end);

    @Query("SELECT SUM(e.quantity) FROM ExportEntity e WHERE e.product.id = :productId AND e.isDeleted = false")
    Integer getTotalExportedQuantity(@Param("productId") Integer productId);

    @Query("SELECT SUM(e.totalExportAmount) FROM ExportEntity e WHERE e.exportDate = :date AND e.isDeleted = false")
    Double sumTotalExportAmountByDate(@Param("date") LocalDate date);

    @Query("SELECT SUM(e.totalExportAmount) FROM ExportEntity e WHERE e.exportDate BETWEEN :from AND :to AND e.isDeleted = false")
    Double sumTotalExportAmountBetween(@Param("from") LocalDate from, @Param("to") LocalDate to);

    Optional<ExportEntity> findTopByProductIdOrderByExportDateDesc(Integer productId);
}
