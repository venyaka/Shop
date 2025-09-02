$(function() {
    // Регистрация
    $('#registerForm').submit(function(e) {
        e.preventDefault();
        var firstName = $('#firstName').val();
        var lastName = $('#lastName').val();
        var email = $('#email').val();
        var password = $('#password').val();
        var confirmPassword = $('#confirmPassword').val();
        if (password !== confirmPassword) {
            $('#registerError').text('Пароли не совпадают').show();
            return;
        }
        $.ajax({
            url: '/api/authorize/register',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({
                firstName: firstName,
                lastName: lastName,
                email: email,
                password: password
            }),
            success: function() {
                $('#registerError').hide();
                $('#registerSuccessMsg').text('На вашу почту отправлено письмо для подтверждения. Пожалуйста, проверьте почту.').show();
                $('#registerForm')[0].reset();
            },
            error: function(xhr) {
                // var err = xhr.responseJSON;
                // var msg = (err && err.message) ? err.message : 'Ошибка регистрации';
                // $('#registerError').text(msg).show();
                $('#registerSuccessMsg').text('На вашу почту отправлено письмо для подтверждения. Пожалуйста, проверьте почту.').show();
            }
        });
    });
});
