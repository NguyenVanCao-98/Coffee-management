package com.viettridao.cafe.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.viettridao.cafe.dto.request.account.UpdateAccountRequest;
import com.viettridao.cafe.model.AccountEntity;
import com.viettridao.cafe.model.EmployeeEntity;
import com.viettridao.cafe.repository.AccountRepository;
import com.viettridao.cafe.repository.EmployeeRepository;
import com.viettridao.cafe.service.AccountService;

import lombok.RequiredArgsConstructor;

/**
 * Triển khai các chức năng liên quan đến tài khoản người dùng.
 * Lớp này xử lý việc cập nhật thông tin tài khoản, bao gồm thông tin nhân viên liên kết và mật khẩu.
 */
@Service
@RequiredArgsConstructor // Tự động tạo constructor với các final field (sử dụng Dependency Injection)

public class AccountServiceImpl implements AccountService {

    // Repository để thao tác với bảng account trong cơ sở dữ liệu
    private final AccountRepository accountRepository;

    // Repository để thao tác với bảng employee trong cơ sở dữ liệu
    private final EmployeeRepository employeeRepository;

    // Dùng để mã hóa mật khẩu trước khi lưu vào cơ sở dữ liệu
    private final PasswordEncoder passwordEncoder;

    /**
     * Cập nhật thông tin tài khoản dựa trên dữ liệu đầu vào.
     * Nếu thông tin nhân viên thay đổi, cập nhật cả employee.
     * Nếu có mật khẩu mới, tiến hành mã hóa và cập nhật.
     *
     * @param request Đối tượng chứa dữ liệu cần cập nhật cho tài khoản
     */
    @Transactional // Đảm bảo tất cả thao tác trong method được thực hiện trong cùng một transaction
    @Override
    public void updateAccount(UpdateAccountRequest request) {
        // Lấy tài khoản từ ID, nếu không tìm thấy sẽ ném lỗi
        AccountEntity account = getAccountById(request.getId());

        // Kiểm tra xem có cần cập nhật thông tin nhân viên không
        if (StringUtils.hasText(request.getAddress()) ||
            StringUtils.hasText(request.getFullName()) ||
            StringUtils.hasText(request.getPhoneNumber())) {

            // Lấy thông tin nhân viên từ tài khoản (mối quan hệ một-một)
            EmployeeEntity employee = account.getEmployee();

            // Nếu chưa có nhân viên (tài khoản mới), khởi tạo mới
            if (employee == null) {
                employee = new EmployeeEntity();
                employee.setAccount(account); // Thiết lập mối quan hệ ngược (từ employee về account)
            }

            // Gán thông tin mới cho nhân viên
            employee.setFullName(request.getFullName());
            employee.setPhoneNumber(request.getPhoneNumber());
            employee.setAddress(request.getAddress());

            // Gán lại quan hệ (đảm bảo không bị mất liên kết)
            employee.setAccount(account);

            // Lưu lại vào database
            employeeRepository.save(employee);

            // Cập nhật quan hệ trong account nếu cần thiết
            account.setEmployee(employee);
        }

        // Kiểm tra nếu có cập nhật mật khẩu
        if (StringUtils.hasText(request.getPassword())) {
            // Mã hóa mật khẩu trước khi lưu
            account.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        // Lưu lại thông tin tài khoản đã cập nhật
        accountRepository.save(account);
    }

    /**
     * Tìm tài khoản theo ID.
     *
     * @param id Mã ID của tài khoản cần tìm
     * @return Tài khoản tương ứng
     * @throws RuntimeException nếu không tìm thấy tài khoản
     */
    @Override
    public AccountEntity getAccountById(Integer id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản có id=" + id));
    }

    /**
     * Tìm tài khoản theo username.
     *
     * @param username Tên đăng nhập của tài khoản
     * @return Tài khoản tương ứng
     * @throws RuntimeException nếu không tìm thấy tài khoản
     */
    @Override
    public AccountEntity getAccountByUsername(String username) {
        return accountRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản có username=" + username));
    }
}
