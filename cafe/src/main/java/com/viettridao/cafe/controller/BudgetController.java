package com.viettridao.cafe.controller;

import java.security.Principal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Locale;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.viettridao.cafe.dto.request.expenses.BudgetFilterRequest;
import com.viettridao.cafe.dto.request.expenses.ExpenseRequest;
import com.viettridao.cafe.dto.response.expenses.BudgetViewResponse;
import com.viettridao.cafe.service.BudgetService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controller xử lý các chức năng liên quan đến quản lý chi tiêu: liệt kê, lọc
 * theo thời gian, và thêm khoản chi tiêu mới.
 */
@Controller
@RequestMapping("/budget")
@RequiredArgsConstructor
public class BudgetController {

	// Inject BudgetService để gọi các nghiệp vụ xử lý chi tiêu
	private final BudgetService budgetService;

	/**
	 * Định dạng số thành tiền tệ theo chuẩn Việt Nam. Nếu là số nguyên thì format
	 * dạng có phân cách hàng nghìn. Nếu là số thập phân thì format đến 2 chữ số sau
	 * dấu phẩy.
	 */
	private String formatCurrency(Double value) {
		if (value == null)
			return "0";
		NumberFormat intFormatter = NumberFormat.getIntegerInstance(new Locale("vi", "VN"));
		DecimalFormat decimalFormatter = new DecimalFormat("#,##0.##");
		return (value % 1 == 0) ? intFormatter.format(value) : decimalFormatter.format(value);
	}

	/**
	 * Hiển thị danh sách chi tiêu với chức năng lọc theo khoảng thời gian. Tính
	 * tổng thu và chi để hiển thị trên giao diện.
	 */
	@GetMapping("/list")
	public String getBudgetList(@ModelAttribute BudgetFilterRequest filter, Model model,
			@ModelAttribute("success") String success, @ModelAttribute("error") String error) {
		// Nếu chưa chọn ngày, mặc định lọc từ 7 ngày trước đến hôm nay
		if (filter.getFromDate() == null || filter.getToDate() == null) {
			LocalDate today = LocalDate.now();
			filter.setToDate(today);
			filter.setFromDate(today.minusDays(7));
		}

		// Kiểm tra nếu ngày bắt đầu lớn hơn ngày kết thúc
		if (filter.getFromDate().isAfter(filter.getToDate())) {
			model.addAttribute("error", "Từ ngày không được lớn hơn đến ngày.");
			model.addAttribute("budgetPage", Page.empty());
			model.addAttribute("filter", filter);
			model.addAttribute("totalIncomeText", "0");
			model.addAttribute("totalExpenseText", "0");
			return "budget/list";
		}

		// Lấy dữ liệu chi tiêu từ service
		Page<BudgetViewResponse> budgetPage = budgetService.getBudgetView(filter);
		model.addAttribute("budgetPage", budgetPage);
		model.addAttribute("filter", filter);

		// Tính tổng thu nhập và chi tiêu trong khoảng thời gian lọc
		double totalIncome = budgetPage.getContent().stream()
				.mapToDouble(item -> item.getIncome() != null ? item.getIncome() : 0.0).sum();
		double totalExpense = budgetPage.getContent().stream()
				.mapToDouble(item -> item.getExpense() != null ? item.getExpense() : 0.0).sum();

		// Định dạng và đưa tổng vào model để hiển thị
		model.addAttribute("totalIncomeText", formatCurrency(totalIncome));
		model.addAttribute("totalExpenseText", formatCurrency(totalExpense));

		// Nếu có thông báo thành công/thất bại từ redirect trước đó thì hiển thị
		if (success != null && !success.isEmpty()) {
			model.addAttribute("success", success);
		}
		if (error != null && !error.isEmpty()) {
			model.addAttribute("error", error);
		}

		return "budget/list"; // Trả về view hiển thị danh sách chi tiêu
	}

	/**
	 * Hiển thị form thêm mới chi tiêu.
	 */
	@GetMapping("/add")
	public String showAddForm(Model model) {
		// Nếu chưa có dữ liệu trong model (từ redirect lỗi), thì khởi tạo mới
		if (!model.containsAttribute("expenseRequest")) {
			model.addAttribute("expenseRequest", new ExpenseRequest());
		}
		return "budget/add"; // Trả về form thêm chi tiêu
	}

	/**
	 * Xử lý thêm mới một khoản chi tiêu.
	 *
	 * @param request   Dữ liệu được gửi từ form
	 * @param result    Kết quả kiểm tra hợp lệ
	 * @param redirect  Gửi thông báo sau redirect
	 * @param principal Lấy thông tin người dùng đang đăng nhập
	 */
	@PostMapping("/add")
	public String handleAddExpense(@Valid @ModelAttribute("expenseRequest") ExpenseRequest request,
			BindingResult result, RedirectAttributes redirect, Principal principal) {
		// Nếu có lỗi validate dữ liệu form, quay lại form kèm thông báo
		if (result.hasErrors()) {
			redirect.addFlashAttribute("org.springframework.validation.BindingResult.expenseRequest", result);
			redirect.addFlashAttribute("expenseRequest", request);
			redirect.addFlashAttribute("error", "Vui lòng kiểm tra lại thông tin chi tiêu.");
			return "redirect:/budget/add";
		}

		// Kiểm tra người dùng đã đăng nhập hay chưa
		if (principal == null) {
			redirect.addFlashAttribute("error", "Người dùng chưa đăng nhập.");
			return "redirect:/budget/add";
		}

		try {
			// Gọi service để thêm chi tiêu mới
			budgetService.addExpense(request, principal.getName());
			redirect.addFlashAttribute("success", "Thêm chi tiêu thành công.");
		} catch (Exception e) {
			// Nếu có lỗi khi xử lý, hiển thị thông báo lỗi
			redirect.addFlashAttribute("error", "Đã có lỗi xảy ra khi thêm chi tiêu.");
			return "redirect:/budget/add";
		}

		// Chuyển hướng về danh sách chi tiêu sau khi thêm thành công
		return "redirect:/budget/list";
	}
}
