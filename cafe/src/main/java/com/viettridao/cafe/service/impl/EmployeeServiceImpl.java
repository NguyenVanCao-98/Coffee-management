package com.viettridao.cafe.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.viettridao.cafe.dto.request.employee.CreateEmployeeRequest;
import com.viettridao.cafe.dto.request.employee.UpdateEmployeeRequest;
import com.viettridao.cafe.dto.response.employee.EmployeePageResponse;
import com.viettridao.cafe.dto.response.employee.EmployeeResponse;
import com.viettridao.cafe.mapper.EmployeeMapper;
import com.viettridao.cafe.model.AccountEntity;
import com.viettridao.cafe.model.EmployeeEntity;
import com.viettridao.cafe.model.PositionEntity;
import com.viettridao.cafe.repository.AccountRepository;
import com.viettridao.cafe.repository.EmployeeRepository;
import com.viettridao.cafe.service.EmployeeService;
import com.viettridao.cafe.service.PositionService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

	private final EmployeeRepository employeeRepository;
	private final PositionService positionService;
	private final AccountRepository accountRepository;
	private final PasswordEncoder passwordEncoder;
	private final EmployeeMapper employeeMapper;

	@Override
	public EmployeePageResponse getAllEmployees(String keyword, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<EmployeeEntity> employeeEntities;

		if (StringUtils.hasText(keyword)) {
			employeeEntities = employeeRepository.getAllEmployeesBySearch(keyword, pageable);
		} else {
			employeeEntities = employeeRepository.getAllEmployees(pageable);
		}

		List<EmployeeResponse> employeeResponses = employeeMapper.toDtoList(employeeEntities.getContent());

		EmployeePageResponse employeePageResponse = new EmployeePageResponse();
		employeePageResponse.setPageNumber(employeeEntities.getNumber());
		employeePageResponse.setPageSize(employeeEntities.getSize());
		employeePageResponse.setTotalElements(employeeEntities.getTotalElements());
		employeePageResponse.setTotalPages(employeeEntities.getTotalPages());
		employeePageResponse.setEmployees(employeeResponses);

		return employeePageResponse;
	}

	 @Transactional
	    @Override
	    public EmployeeEntity createEmployee(CreateEmployeeRequest request) {
	        EmployeeEntity employee = new EmployeeEntity();
	        employee.setFullName(request.getFullName().trim());
	        employee.setPhoneNumber(request.getPhoneNumber().trim());
	        employee.setAddress(request.getAddress().trim());
	        employee.setIsDeleted(false);

	        if (request.getPositionId() != null) {
	            PositionEntity position = positionService.getPositionById(request.getPositionId());
	            employee.setPosition(position);
	        }

	        boolean hasUsername = StringUtils.hasText(request.getUsername());
	        boolean hasPassword = StringUtils.hasText(request.getPassword());

	        if (hasUsername || hasPassword) {
	            if (!hasUsername || !hasPassword) {
	                throw new RuntimeException("Phải nhập đầy đủ cả tên đăng nhập và mật khẩu!");
	            }

	            validateCredentials(request.getUsername(), request.getPassword());

	            String username = request.getUsername().trim();
	            if (accountRepository.findByUsername(username).isPresent()) {
	                throw new RuntimeException("Tên đăng nhập đã tồn tại!");
	            }

	            AccountEntity account = new AccountEntity();
	            account.setUsername(username);
	            account.setPassword(passwordEncoder.encode(request.getPassword().trim()));
	            account.setImageUrl(request.getImageUrl().trim());
	            account.setIsDeleted(false);
	            account.setPermission("EMPLOYEE");

	            try {
	                accountRepository.save(account);
	            } catch (DataIntegrityViolationException ex) {
	                throw new RuntimeException("Tên đăng nhập đã tồn tại trong hệ thống!");
	            }

	            employee.setAccount(account);
	        }

	        return employeeRepository.save(employee);
	    }

	@Transactional
	@Override
	public void deleteEmployee(Integer id) {
		EmployeeEntity employee = getEmployeeById(id);
		employee.setIsDeleted(true);
		employeeRepository.save(employee);
	}

	@Transactional
	@Override
	public void updateEmployee(UpdateEmployeeRequest request) {
		EmployeeEntity employee = getEmployeeById(request.getId());

		employee.setFullName(request.getFullName().trim());
		employee.setPhoneNumber(request.getPhoneNumber().trim());
		employee.setAddress(request.getAddress().trim());

		if (request.getPositionId() != null) {
			PositionEntity position = positionService.getPositionById(request.getPositionId());
			employee.setPosition(position);
		}

		if (StringUtils.hasText(request.getUsername()) && StringUtils.hasText(request.getPassword())) {
			validateCredentials(request.getUsername(), request.getPassword());

			String username = request.getUsername().trim();
			Optional<AccountEntity> optionalAccount = accountRepository.findByUsername(username);

			if (optionalAccount.isPresent()) {
				AccountEntity existingAccount = optionalAccount.get();

				if (!existingAccount.getEmployee().getId().equals(employee.getId())) {
					throw new RuntimeException("Tên đăng nhập đã tồn tại cho nhân viên khác!");
				}

				existingAccount.setPassword(passwordEncoder.encode(request.getPassword().trim()));
				existingAccount.setImageUrl(request.getImageUrl().trim());
				accountRepository.save(existingAccount);
			} else {
				AccountEntity newAccount = new AccountEntity();
				newAccount.setUsername(username);
				newAccount.setPassword(passwordEncoder.encode(request.getPassword().trim()));
				newAccount.setPermission("EMPLOYEE");
				newAccount.setEmployee(employee);

				if (StringUtils.hasText(request.getImageUrl())) {
					newAccount.setImageUrl(request.getImageUrl().trim());
				}

				accountRepository.save(newAccount);
				employee.setAccount(newAccount);
			}
		}

		employeeRepository.save(employee);
	}

	@Override
	public EmployeeEntity getEmployeeById(Integer id) {
		return employeeRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên có id = " + id));
	}

	/**
	 * Kiểm tra username ≠ password và độ mạnh của mật khẩu
	 */
	private void validateCredentials(String username, String password) {
		if (username.trim().equals(password.trim())) {
			throw new RuntimeException("Tên đăng nhập và mật khẩu không được trùng nhau!");
		}

		if (!isStrongPassword(password)) {
			throw new RuntimeException("Mật khẩu phải có ít nhất 6 ký tự, bao gồm chữ hoa, chữ thường, số và ký tự đặc biệt!");
		}
	}

	/**
	 * Kiểm tra độ mạnh của mật khẩu
	 */
	private boolean isStrongPassword(String password) {
		if (password == null || password.length() < 6) {
			return false;
		}

		boolean hasUppercase = false;
		boolean hasLowercase = false;
		boolean hasDigit = false;
		boolean hasSpecialChar = false;

		for (char c : password.toCharArray()) {
			if (Character.isUpperCase(c)) hasUppercase = true;
			else if (Character.isLowerCase(c)) hasLowercase = true;
			else if (Character.isDigit(c)) hasDigit = true;
			else if ("!@#$%^&*()-_=+[]{}|;:'\",.<>?/`~".contains(String.valueOf(c))) hasSpecialChar = true;
		}

		return hasUppercase && hasLowercase && hasDigit && hasSpecialChar;
	}
}
