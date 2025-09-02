$(function() {
    // Загрузка информации о пользователе
    function loadProfile() {
        $.get('/api/users/info', function(data) {
            $('#profileFirstName').text(data.firstName || '-');
            $('#profileLastName').text(data.lastName || '-');
            $('#profileEmail').text(data.email || '-');
            $('#editFirstName').val(data.firstName || '');
            $('#editLastName').val(data.lastName || '');
        }).fail(function(xhr) {
            if (xhr.status === 401) {
                window.location.href = '/login';
            } else {
                var err = xhr.responseJSON;
                $('#profileEditError').text(err && err.message ? err.message : 'Ошибка').show();
            }
        });
    }

    // Сохранение изменений профиля
    $('#profileEditForm').submit(function(e) {
        e.preventDefault();
        var formData = new FormData();
        formData.append('firstName', $('#editFirstName').val());
        formData.append('lastName', $('#editLastName').val());
        $.ajax({
            url: '/api/users/update',
            type: 'PATCH',
            data: formData,
            processData: false,
            contentType: false,
            success: function(data) {
                loadProfile();
                $('#profileEditError').hide();
            },
            error: function(xhr) {
                var err = xhr.responseJSON;
                $('#profileEditError').text(err && err.message ? err.message : 'Ошибка').show();
            }
        });
    });

    // Выход из аккаунта
    $('#logoutBtn').click(function() {
        $.post('/api/users/logout', function() {
            window.location.href = '/login';
        });
    });

    // Инициализация
    loadProfile();
});
