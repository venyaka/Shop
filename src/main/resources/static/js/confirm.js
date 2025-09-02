$(function() {
    // Подтверждение email
    var urlParams = new URLSearchParams(window.location.search);
    var token = urlParams.get('token');
    var email = urlParams.get('email');
    if (token && email) {
        $.ajax({
            url: '/api/authorize/verification',
            type: 'POST',
            data: { email: email, token: token },
            success: function() {
                $('#confirmMsg').removeClass('alert-info').addClass('alert-success').text('Email успешно подтвержден! Перенаправление...');
                // После подтверждения предложим войти
                setTimeout(function() { window.location.href = '/login'; }, 1500);
            },
            error: function(xhr) {
                var err = xhr.responseJSON;
                $('#confirmMsg').removeClass('alert-info').addClass('alert-danger').text(err && err.message ? err.message : 'Ошибка подтверждения');
            }
        });
    } else {
        $('#confirmMsg').removeClass('alert-info').addClass('alert-danger').text('Некорректная ссылка подтверждения.');
    }
});
