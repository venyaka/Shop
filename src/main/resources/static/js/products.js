$(function() {
    // Загрузка категорий для фильтра
    function loadCategories() {
        $.ajax({
            url: '/api/categories',
            type: 'GET',
            success: function(data) {
                var select = $('#filterCategory');
                select.empty();
                select.append('<option value="">Все категории</option>');
                data.forEach(function(cat) {
                    select.append('<option value="' + cat.id + '">' + cat.name + '</option>');
                });
            },
            error: function(xhr) {
                var err = xhr.responseJSON;
                $('#productsError').text(err && err.message ? err.message : 'Ошибка').show();
            }
        });
    }

    // Загрузка товаров с фильтрами
    function loadProducts(filters) {
        var params = $.param(filters);
        $.ajax({
            url: '/api/products' + (params ? ('?' + params) : ''),
            type: 'GET',
            success: function(data) {
                var list = $('#productsList');
                list.empty();
                if (data.length === 0) {
                    list.append('<div class="col-12 text-center text-muted">Нет товаров по выбранным параметрам</div>');
                    return;
                }
                data.forEach(function(prod) {
                    if (!prod.isActive) return;
                    var img = prod.imageUrl ? '<img src="' + prod.imageUrl + '" class="img-thumbnail" style="max-width:150px;max-height:150px;" alt="' + prod.name + '">' : '';
                    list.append('<div class="col-md-4"'
                        + '>'
                        + '<div class="card h-100">'
                        + (img ? '<div class="text-center mt-3">' + img + '</div>' : '')
                        + '<div class="card-body">'
                        + '<h5 class="card-title">' + prod.name + '</h5>'
                        + '<p class="card-text">Категория: ' + (prod.categoryName || '-') + '</p>'
                        + '<p class="card-text">Цена: <b>' + prod.price + ' ₽</b></p>'
                        + '<a href="/products/' + prod.id + '" class="btn btn-outline-primary">Подробнее</a>'
                        + '</div></div></div>');
                });
            },
            error: function(xhr) {
                var err = xhr.responseJSON;
                $('#productsError').text(err && err.message ? err.message : 'Ошибка').show();
            }
        });
    }

    // Фильтрация товаров
    $('#productFilterForm').submit(function(e) {
        e.preventDefault();
        var filters = {
            name: $('#searchName').val(),
            categoryId: $('#filterCategory').val(),
            priceFrom: $('#priceFrom').val(),
            priceTo: $('#priceTo').val()
        };
        loadProducts(filters);
    });

    // Инициализация
    loadCategories();
    loadProducts({});
});
