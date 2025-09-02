$(function() {
    // Авторизация
    $('#loginForm').submit(function(e) {
        e.preventDefault();
        var email = $('#email').val();
        var password = $('#password').val();
        $.ajax({
            url: '/api/authorize/login',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ email: email, password: password }),
            success: function() {
                $('#loginError').hide();
                // После 200 OK попробуем получить роли и решить куда редиректить
                $.get('/api/users/info', function(user) {
                    if (user && user.roles && user.roles.includes('ADMIN')) {
                        window.location.href = '/admin';
                    } else {
                        window.location.href = '/';
                    }
                }).fail(function() {
                    // Если ещё не доступно (например, задержка установки cookies), просто на профиль
                    window.location.href = '/';
                });
            },
            error: function(xhr) {
                var err = xhr.responseJSON;
                $('#loginError').text(err && err.message ? err.message : 'Ошибка').show();
            }
        });
    });
});
