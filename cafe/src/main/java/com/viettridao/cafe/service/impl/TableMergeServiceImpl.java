package com.viettridao.cafe.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.viettridao.cafe.common.InvoiceStatus;
import com.viettridao.cafe.common.TableStatus;
import com.viettridao.cafe.model.EmployeeEntity;
import com.viettridao.cafe.model.InvoiceDetailEntity;
import com.viettridao.cafe.model.InvoiceEntity;
import com.viettridao.cafe.model.InvoiceKey;
import com.viettridao.cafe.model.ReservationEntity;
import com.viettridao.cafe.model.ReservationKey;
import com.viettridao.cafe.model.TableEntity;
import com.viettridao.cafe.repository.EmployeeRepository;
import com.viettridao.cafe.repository.InvoiceDetailRepository;
import com.viettridao.cafe.repository.InvoiceRepository;
import com.viettridao.cafe.repository.ReservationRepository;
import com.viettridao.cafe.repository.TableRepository;
import com.viettridao.cafe.service.TableMergeService;

import lombok.RequiredArgsConstructor;

/**
 * Service xử lý nghiệp vụ gộp bàn (merge tables), bao gồm gộp hóa đơn, chi tiết
 * hóa đơn, đặt bàn và cập nhật trạng thái bàn.
 */
@Service
@RequiredArgsConstructor
//Khai báo lớp triển khai TableMergeService
public class TableMergeServiceImpl implements TableMergeService {

 // Khai báo các repository để thao tác với CSDL
 private final TableRepository tableRepository;
 private final InvoiceRepository invoiceRepository;
 private final InvoiceDetailRepository invoiceDetailRepository;
 private final ReservationRepository reservationRepository;
 private final EmployeeRepository employeeRepository;

 // Ghi đè phương thức mergeTables từ interface
 @Override
 @Transactional // Đảm bảo toàn bộ quá trình gộp bàn là một giao dịch (transaction)
 public void mergeTables(Integer targetTableId, List<Integer> sourceTableIds, String customerName,
                         String customerPhone) {

     // Lấy thông tin bàn đích theo ID, nếu không tìm thấy thì ném lỗi
     TableEntity targetTable = tableRepository.findById(targetTableId)
             .orElseThrow(() -> new RuntimeException("Không tìm thấy bàn gộp đến."));

     // Lấy trạng thái hiện tại của bàn đích (AVAIABLE, OCCUPIED, RESERVED)
     TableStatus targetStatus = targetTable.getStatus();

     // Nếu bàn đích đang ở trạng thái RESERVED (đã đặt trước) thì không được gộp
     if (targetStatus == TableStatus.RESERVED) {
         throw new RuntimeException("Không thể gộp vào bàn đã đặt trước.");
     }

     // Tìm hóa đơn chưa thanh toán gần nhất gắn với bàn đích
     InvoiceEntity targetInvoice = invoiceRepository
             .findTopByReservations_Table_IdAndStatusAndIsDeletedFalseOrderByCreatedAtDesc(targetTableId,
                     InvoiceStatus.UNPAID);

     // Nếu bàn đích đang ở trạng thái trống → cần tạo mới hóa đơn
     if (targetStatus == TableStatus.AVAILABLE) {
         // Dữ liệu tên và số điện thoại khách mặc định là truyền vào
         String finalName = customerName;
         String finalPhone = customerPhone;

         // Nếu chỉ có một bàn nguồn → ưu tiên lấy tên và SĐT từ bàn đó
         if (sourceTableIds.size() == 1) {
             Integer sourceId = sourceTableIds.get(0); // ID bàn nguồn duy nhất
             InvoiceEntity sourceInvoice = invoiceRepository
                     .findTopByReservations_Table_IdAndStatusAndIsDeletedFalseOrderByCreatedAtDesc(sourceId,
                             InvoiceStatus.UNPAID);
             if (sourceInvoice != null) {
                 // Lấy danh sách đặt chỗ liên kết với hóa đơn nguồn
                 List<ReservationEntity> srcReservations = reservationRepository
                         .findByInvoice_IdAndIsDeletedFalse(sourceInvoice.getId());
                 if (!srcReservations.isEmpty()) {
                     // Lấy tên và SĐT từ đặt chỗ đầu tiên
                     finalName = srcReservations.get(0).getCustomerName();
                     finalPhone = srcReservations.get(0).getCustomerPhone();
                 }
             }
         }

         // Tạo mới hóa đơn cho bàn đích
         targetInvoice = new InvoiceEntity();
         targetInvoice.setStatus(InvoiceStatus.UNPAID);         // Trạng thái chưa thanh toán
         targetInvoice.setCreatedAt(LocalDateTime.now());       // Ngày giờ tạo
         targetInvoice.setIsDeleted(false);                     // Chưa bị xóa
         targetInvoice.setTotalAmount(0.0);                     // Tổng tiền ban đầu = 0
         invoiceRepository.save(targetInvoice);                 // Lưu vào CSDL

         // Lấy thông tin nhân viên mặc định có ID = 1
         EmployeeEntity employee = employeeRepository.findById(1)
                 .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên."));

         // Tạo mới bản ghi đặt chỗ
         ReservationEntity reservation = new ReservationEntity();
         ReservationKey key = new ReservationKey();             // Tạo khóa chính phức hợp
         key.setIdInvoice(targetInvoice.getId());
         key.setIdTable(targetTableId);
         key.setIdEmployee(employee.getId());

         // Thiết lập thông tin đặt chỗ
         reservation.setId(key);
         reservation.setInvoice(targetInvoice);
         reservation.setTable(targetTable);
         reservation.setEmployee(employee);
         reservation.setCustomerName(finalName);
         reservation.setCustomerPhone(finalPhone);
         reservation.setReservationDate(LocalDate.now());
         reservation.setReservationTime(LocalTime.now());
         reservation.setIsDeleted(false);

         // Lưu đặt chỗ vào CSDL
         reservationRepository.save(reservation);

         // Đánh dấu bàn đích là đang được sử dụng
         targetTable.setStatus(TableStatus.OCCUPIED);
         tableRepository.save(targetTable);

     } else if (targetStatus == TableStatus.OCCUPIED && targetInvoice == null) {
         // Nếu bàn đang sử dụng nhưng không có hóa đơn → lỗi
         throw new RuntimeException("Bàn đang phục vụ nhưng chưa có hóa đơn.");
     }

     // Đếm số lượng bàn nguồn đang ở trạng thái OCCUPIED
     long occupiedSourceCount = sourceTableIds.stream()
             .map(id -> tableRepository.findById(id).orElse(null))           // Lấy từng bàn
             .filter(t -> t != null && t.getStatus() == TableStatus.OCCUPIED) // Lọc bàn đang OCCUPIED
             .count();

     // Nếu bàn đích đang OCCUPIED và có nhiều bàn nguồn cũng OCCUPIED → cập nhật lại tên khách
     if (targetStatus == TableStatus.OCCUPIED && targetInvoice != null && occupiedSourceCount > 1) {
         List<ReservationEntity> reservations = reservationRepository
                 .findByInvoice_IdAndIsDeletedFalse(targetInvoice.getId());
         for (ReservationEntity r : reservations) {
             r.setCustomerName(customerName);
             r.setCustomerPhone(customerPhone);
         }
         reservationRepository.saveAll(reservations); // Lưu lại thay đổi
     }

     // Map để lưu chi tiết món ăn đã tồn tại theo ID món
     Map<Integer, InvoiceDetailEntity> mergedDetails = new HashMap<>();

     // Duyệt qua các chi tiết hóa đơn cũ của bàn đích
     if (targetInvoice.getInvoiceDetails() != null) {
         for (InvoiceDetailEntity detail : targetInvoice.getInvoiceDetails()) {
             if (Boolean.TRUE.equals(detail.getIsDeleted()))
                 continue; // Bỏ qua món đã bị đánh dấu xóa
             mergedDetails.put(detail.getMenuItem().getId(), detail); // Ghi nhận món vào map
         }
     }

     // Gộp dữ liệu từ các bàn nguồn
     for (Integer sourceId : sourceTableIds) {
         if (sourceId.equals(targetTableId))
             continue; // Bỏ qua nếu là chính bàn đích

         TableEntity sourceTable = tableRepository.findById(sourceId)
                 .orElseThrow(() -> new RuntimeException("Không tìm thấy bàn nguồn."));

         // Nếu bàn nguồn là bàn đã đặt trước → không cho gộp
         if (sourceTable.getStatus() == TableStatus.RESERVED) {
             throw new RuntimeException("Không thể gộp từ bàn đã đặt trước.");
         }

         // Tìm hóa đơn chưa thanh toán của bàn nguồn
         InvoiceEntity sourceInvoice = invoiceRepository
                 .findTopByReservations_Table_IdAndStatusAndIsDeletedFalseOrderByCreatedAtDesc(sourceId,
                         InvoiceStatus.UNPAID);

         // Nếu không có hóa đơn hoặc không có món ăn → bỏ qua
         if (sourceInvoice == null || sourceInvoice.getInvoiceDetails() == null)
             continue;

         // Duyệt qua từng món trong hóa đơn nguồn
         for (InvoiceDetailEntity detail : sourceInvoice.getInvoiceDetails()) {
             if (Boolean.TRUE.equals(detail.getIsDeleted()))
                 continue; // Bỏ qua món đã bị xóa

             Integer menuItemId = detail.getMenuItem().getId();

             if (mergedDetails.containsKey(menuItemId)) {
                 // Nếu món đã có trong hóa đơn đích → cộng dồn số lượng
                 InvoiceDetailEntity existing = mergedDetails.get(menuItemId);
                 existing.setQuantity(existing.getQuantity() + detail.getQuantity());
                 invoiceDetailRepository.save(existing);
             } else {
                 // Nếu chưa có → thêm mới vào hóa đơn đích
                 InvoiceDetailEntity newDetail = new InvoiceDetailEntity();
                 InvoiceKey newKey = new InvoiceKey();
                 newKey.setIdInvoice(targetInvoice.getId());
                 newKey.setIdMenuItem(menuItemId);

                 newDetail.setId(newKey);
                 newDetail.setInvoice(targetInvoice);
                 newDetail.setMenuItem(detail.getMenuItem());
                 newDetail.setQuantity(detail.getQuantity());
                 newDetail.setPrice(detail.getPrice());
                 newDetail.setIsDeleted(false);

                 invoiceDetailRepository.save(newDetail); // Lưu món mới
                 mergedDetails.put(menuItemId, newDetail); // Thêm vào map
             }

             // Đánh dấu chi tiết hóa đơn nguồn là đã xóa
             detail.setIsDeleted(true);
             invoiceDetailRepository.save(detail);
         }

         // Đánh dấu hóa đơn cũ là đã xóa
         sourceInvoice.setIsDeleted(true);
         invoiceRepository.save(sourceInvoice);

         // Đánh dấu các đặt chỗ cũ là đã xóa
         List<ReservationEntity> oldReservations = reservationRepository
                 .findByInvoice_IdAndIsDeletedFalse(sourceInvoice.getId());
         for (ReservationEntity old : oldReservations) {
             old.setIsDeleted(true);
         }
         reservationRepository.saveAll(oldReservations);

         // Cập nhật bàn nguồn thành AVAILABLE (trống)
         sourceTable.setStatus(TableStatus.AVAILABLE);
         tableRepository.save(sourceTable);
     }

     // Tính tổng tiền mới cho hóa đơn đích
     double totalAmount = mergedDetails.values().stream()
             .filter(d -> d.getIsDeleted() == null || !d.getIsDeleted()) // Chỉ lấy món chưa bị xóa
             .mapToDouble(d -> d.getQuantity() * d.getPrice())            // Tính tiền từng món
             .sum();                                                      // Cộng tổng

     // Cập nhật lại tổng tiền cho hóa đơn đích
     targetInvoice.setTotalAmount(totalAmount);
     invoiceRepository.save(targetInvoice); // Lưu
 }
}
