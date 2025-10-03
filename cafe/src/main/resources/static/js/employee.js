function formatVND(number) {
    if (!number) return '';
    return Number(number).toLocaleString('vi-VN') + ' VNĐ';
}

function updateSalaryDisplay() {
    const select = document.getElementById('positionSelect');
    const salary = select.options[select.selectedIndex].getAttribute('data-luong');
    document.getElementById('salaryInput').value = salary || '';
    document.getElementById('salaryDisplay').textContent = formatVND(salary);
}

document.getElementById('positionSelect').addEventListener('change', updateSalaryDisplay);

window.addEventListener('DOMContentLoaded', updateSalaryDisplay);

document.getElementById('avatarInput').addEventListener('change', function (event) {
    const reader = new FileReader();
    reader.onload = function (e) {
        document.getElementById('avatarPreview').src = e.target.result;
    };
    reader.readAsDataURL(event.target.files[0]);
});