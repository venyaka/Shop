$(function() {
    // Проверка авторизации и ролей для навигации (только UI; доступ контролирует бэкенд)
    $.get('/api/users/info', function(user) {
        if (user && user.id) {
            $('#nav-login, #btn-login').addClass('d-none');
            $('#nav-profile, #btn-profile').removeClass('d-none');
            if (user.roles && user.roles.includes('ADMIN')) {
                $('#nav-admin, #btn-admin').removeClass('d-none');
            } else {
                $('#nav-admin, #btn-admin').addClass('d-none');
            }
        } else {
            $('#nav-login, #btn-login').removeClass('d-none');
            $('#nav-profile, #btn-profile').addClass('d-none');
            $('#nav-admin, #btn-admin').addClass('d-none');
        }
    }).fail(function() {
        $('#nav-login, #btn-login').removeClass('d-none');
        $('#nav-profile, #btn-profile').addClass('d-none');
        $('#nav-admin, #btn-admin').addClass('d-none');
    });
});
