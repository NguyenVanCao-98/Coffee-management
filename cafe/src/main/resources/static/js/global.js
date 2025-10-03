document.querySelectorAll('.btn-delete').forEach(button => {
    button.addEventListener('click', function () {
        const name = this.getAttribute('data-name') || 'khuyến mãi này';
        Swal.fire({
            title: 'Xác nhận xoá?',
            text: `Bạn có chắc muốn xóa "${name}" không?`,
            icon: 'question',
            showCancelButton: true,
            confirmButtonText: 'Có',
            cancelButtonText: 'Không',
            confirmButtonColor: '#16a34a',
            cancelButtonColor: '#ef4444'
        }).then((result) => {
            if (result.isConfirmed) {
                const form = this.closest('form');
                if (form) form.submit();
            }
        });
    });
});
