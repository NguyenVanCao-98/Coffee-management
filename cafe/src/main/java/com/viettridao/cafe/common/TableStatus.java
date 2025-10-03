package com.viettridao.cafe.common;

/**
 * Enum TableStatus đại diện cho các trạng thái có thể có của một bàn trong quán
 * cà phê.
 */
public enum TableStatus {

	// Bàn đang trống, chưa có khách sử dụng.
	AVAILABLE,

	// Bàn đang có khách sử dụng.
	OCCUPIED,

	// Bàn đã được khách đặt trước.
	RESERVED
}
