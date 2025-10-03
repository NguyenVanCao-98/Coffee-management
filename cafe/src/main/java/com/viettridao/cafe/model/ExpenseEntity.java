package com.viettridao.cafe.model;

import java.time.LocalDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "expenses") // chitieu
public class ExpenseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "expense_id")
	private Integer id;

	@Column(name = "amount")
	private Double amount;

	@Column(name = "expense_name")
	private String expenseName;

	@Column(name = "expense_date")
	private LocalDate expenseDate;

	@Column(name = "is_deleted")
	private Boolean isDeleted;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "account_id")
	private AccountEntity account;
}
