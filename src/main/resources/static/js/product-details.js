$(function() {
    // Получение ID товара из Thymeleaf
    var productId = window.location.pathname.split('/').pop();

    function loadProductDetails() {
        $.get('/api/products', function(data) {
            var prod = data.find(function(p) { return p.id == productId; });
            if (!prod) {
                $('#productDetails').html('<div class="col-12 text-center text-danger">Товар не найден</div>');
                return;
            }
            var img = prod.imageUrl ? '<img src="' + prod.imageUrl + '" class="img-fluid mb-3" style="max-width:350px;max-height:350px;">' : '';
            var status = prod.isActive ? 'Активен' : 'Не активен';
            var statusBlock = '';
            // Проверка роли ADMIN через /api/users/info
            $.get('/api/users/info', function(user) {
                if (user && user.id && user.email) {
                    // Если ADMIN, показываем статус
                    $.get('/api/users/info', function(userInfo) {
                        if (userInfo && userInfo.roles && userInfo.roles.includes('ADMIN')) {
                            statusBlock = '<div class="mb-2"><b>Статус:</b> ' + status + '</div>';
                        }
                        renderProduct(prod, img, statusBlock);
                    });
                } else {
                    renderProduct(prod, img, '');
                }
            }).fail(function() {
                renderProduct(prod, img, '');
            });
        }).fail(function(xhr) {
            var err = xhr.responseJSON;
            $('#productError').text(err && err.message ? err.message : 'Ошибка').show();
        });
    }

    function renderProduct(prod, img, statusBlock) {
        var html = '<div class="col-md-8">'
            + '<div class="card">'
            + (img ? '<div class="text-center mt-3">' + img + '</div>' : '')
            + '<div class="card-body">'
            + '<h3 class="card-title">' + prod.name + '</h3>'
            + '<div class="mb-2"><b>Категория:</b> ' + (prod.categoryName || '-') + '</div>'
            + '<div class="mb-2"><b>Цена:</b> ' + prod.price + ' ₽</div>'
            + statusBlock
            + '<div class="mb-2"><b>Дата добавления:</b> ' + (prod.createdAt ? prod.createdAt : '-') + '</div>'
            + '<div class="mb-2"><b>Описание:</b><br>' + (prod.description || '-') + '</div>'
            + '</div></div></div>';
        $('#productDetails').html(html);
    }

    loadProductDetails();
});

