package com.viettridao.cafe.model;

import java.util.List;

import com.viettridao.cafe.common.TableStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "tables") // ban
public class TableEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	// Đánh dấu `id` là khóa chính và sẽ được tự động tăng (auto-increment) bởi cơ sở dữ liệu.
	@Column(name = "table_id")
	private Integer id;
	// Trường đại diện cho ID của bàn, ánh xạ với cột `table_id` trong bảng CSDL.

	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private TableStatus status;
	// Trạng thái của bàn (ví dụ: Đang sử dụng, Trống...), được lưu dưới dạng chuỗi trong CSDL.

	@Column(name = "table_name")
	private String tableName;
	// Tên hiển thị của bàn (ví dụ: Bàn 1, Bàn VIP...), ánh xạ với cột `table_name`.

	@Column(name = "is_deleted")
	private Boolean isDeleted;
	// Cờ đánh dấu bàn đã bị xóa mềm hay chưa (true = đã xóa, false = còn tồn tại).

	@OneToMany(mappedBy = "table", cascade = CascadeType.ALL)
	private List<ReservationEntity> reservations;
	// Quan hệ 1-nhiều: Một bàn có thể có nhiều đặt chỗ (reservation).
	// `mappedBy = "table"` nghĩa là quan hệ được ánh xạ ngược từ trường `table` trong lớp `ReservationEntity`.
	// `cascade = CascadeType.ALL` cho phép tự động thao tác trên các `reservations` khi thao tác với `table` (ví dụ: xóa bàn sẽ xóa luôn đặt chỗ liên quan).

}
