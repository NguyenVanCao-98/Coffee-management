package com.viettridao.cafe.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.viettridao.cafe.common.TableStatus;
import com.viettridao.cafe.model.TableEntity;
import com.viettridao.cafe.repository.TableRepository;
import com.viettridao.cafe.service.TableMergeService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/sale")
@RequiredArgsConstructor
public class TableMergeController {

    private final TableRepository tableRepository;
    private final TableMergeService tableMergeService;

    @GetMapping("/merge-tables")
    public String showMergePage(Model model) {
        List<TableEntity> allTables = tableRepository.findByIsDeletedFalse();

        // Bàn mục tiêu: AVAILABLE + OCCUPIED
        List<TableEntity> targetTables = allTables.stream()
                .filter(t -> t.getStatus() == TableStatus.AVAILABLE || t.getStatus() == TableStatus.OCCUPIED)
                .collect(Collectors.toList());

        // Bàn nguồn: OCCUPIED
        List<TableEntity> sourceTables = allTables.stream()
                .filter(t -> t.getStatus() == TableStatus.OCCUPIED)
                .collect(Collectors.toList());

        model.addAttribute("targetTables", targetTables);
        model.addAttribute("sourceTables", sourceTables);
        return "sale/merge-tables";
    }

    @PostMapping("/merge-tables")
    public String mergeTables(@RequestParam("targetTableId") Integer targetId,
                              @RequestParam(value = "sourceTableIds", required = false) List<Integer> sourceIds,
                              @RequestParam("customerName") String customerName,
                              @RequestParam("customerPhone") String customerPhone,
                              RedirectAttributes redirectAttributes,
                              Model model) {

        // Lấy lại danh sách để hiển thị khi có lỗi
        List<TableEntity> allTables = tableRepository.findByIsDeletedFalse();
        model.addAttribute("targetTables", allTables.stream()
                .filter(t -> t.getStatus() == TableStatus.AVAILABLE || t.getStatus() == TableStatus.OCCUPIED)
                .collect(Collectors.toList()));
        model.addAttribute("sourceTables", allTables.stream()
                .filter(t -> t.getStatus() == TableStatus.OCCUPIED)
                .collect(Collectors.toList()));

        // Giữ lại dữ liệu user nhập
        model.addAttribute("targetTableId", targetId);
        model.addAttribute("sourceTableIds", sourceIds);
        model.addAttribute("customerName", customerName);
        model.addAttribute("customerPhone", customerPhone);

        try {
        	if (sourceIds == null || sourceIds.size() < 2) {
        	    model.addAttribute("error", "Vui lòng chọn ít nhất 2 bàn nguồn để gộp.");
        	    return "sale/merge-tables";
        	}

            TableEntity targetTable = tableRepository.findById(targetId).orElse(null);
            if (targetTable == null) {
                model.addAttribute("error", "Không tìm thấy bàn đích.");
                return "sale/merge-tables";
            }
            if (targetTable.getStatus() == TableStatus.RESERVED) {
                model.addAttribute("error", "Không thể gộp vào bàn đã được đặt trước.");
                return "sale/merge-tables";
            }

            for (Integer sourceId : sourceIds) {
                TableEntity sourceTable = tableRepository.findById(sourceId).orElse(null);
                if (sourceTable == null) {
                    model.addAttribute("error", "Không tìm thấy bàn nguồn.");
                    return "sale/merge-tables";
                }
                if (sourceTable.getStatus() == TableStatus.RESERVED) {
                    model.addAttribute("error", "Không thể gộp từ bàn đã được đặt trước.");
                    return "sale/merge-tables";
                }
            }

            boolean targetIsOccupied = targetTable.getStatus() == TableStatus.OCCUPIED;
            boolean hasSourceOccupied = sourceIds.stream()
                    .map(id -> tableRepository.findById(id).orElse(null))
                    .anyMatch(t -> t != null && t.getStatus() == TableStatus.OCCUPIED);

            boolean needCustomerInfo = (sourceIds.size() > 1)
                    || (targetIsOccupied && !hasSourceOccupied)
                    || (!targetIsOccupied && hasSourceOccupied)
                    || (targetIsOccupied && hasSourceOccupied);

            if (needCustomerInfo && (customerName == null || customerName.trim().isEmpty()
                    || customerPhone == null || customerPhone.trim().isEmpty())) {
                model.addAttribute("error", "Vui lòng nhập Tên khách và Số điện thoại theo yêu cầu gộp.");
                return "sale/merge-tables";
            }

            tableMergeService.mergeTables(targetId, sourceIds, customerName, customerPhone);

            redirectAttributes.addFlashAttribute("success", "Gộp bàn thành công.");
            return "redirect:/sale";

        } catch (Exception e) {
            model.addAttribute("error", "❌ " + e.getMessage());
            return "sale/merge-tables";
        }
    }
}
