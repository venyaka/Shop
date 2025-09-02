$(function() {
    // Загрузка категорий
    function loadCategories() {
        $.ajax({
            url: '/api/categories',
            type: 'GET',
            success: function(data) {
                var tbody = $('#categoriesList');
                tbody.empty();
                data.forEach(function(cat) {
                    tbody.append('<tr>' +
                        '<td>' + cat.id + '</td>' +
                        '<td>' + cat.name + '</td>' +
                        '<td>' + (cat.description || '-') + '</td>' +
                        '<td>' +
                            '<button class="btn btn-sm btn-primary me-1 editCategoryBtn" data-id="' + cat.id + '">Редактировать</button>' +
                            '<button class="btn btn-sm btn-danger deleteCategoryBtn" data-id="' + cat.id + '">Удалить</button>' +
                        '</td>' +
                    '</tr>');
                });
            },
            error: function(xhr) {
                var err = xhr.responseJSON;
                $('#categoriesError').text(err && err.message ? err.message : 'Ошибка').show();
            }
        });
    }

    // Открытие модального окна для создания категории
    $('#createCategoryBtn').click(function() {
        $('#categoryForm')[0].reset();
        $('#categoryId').val('');
        $('#categoryModal').modal('show');
    });

    // Открытие модального окна для редактирования категории
    $(document).on('click', '.editCategoryBtn', function() {
        var id = $(this).data('id');
        $.ajax({
            url: '/api/categories',
            type: 'GET',
            success: function(data) {
                var cat = data.find(function(c) { return c.id === id; });
                if (cat) {
                    $('#categoryId').val(cat.id);
                    $('#categoryName').val(cat.name);
                    $('#categoryDescription').val(cat.description);
                    $('#categoryModal').modal('show');
                }
            }
        });
    });

    // Сохранение категории (создание/редактирование)
    $('#categoryForm').submit(function(e) {
        e.preventDefault();
        var id = $('#categoryId').val();
        var data = {
            name: $('#categoryName').val(),
            description: $('#categoryDescription').val()
        };
        var url = id ? '/api/categories/' + id : '/api/categories';
        var method = id ? 'PUT' : 'POST';
        $.ajax({
            url: url,
            type: method,
            contentType: 'application/json',
            data: JSON.stringify(data),
            success: function() {
                $('#categoryModal').modal('hide');
                loadCategories();
            },
            error: function(xhr) {
                var err = xhr.responseJSON;
                $('#categoryFormError').text(err && err.message ? err.message : 'Ошибка').show();
            }
        });
    });

    // Удаление категории
    $(document).on('click', '.deleteCategoryBtn', function() {
        if (!confirm('Удалить категорию? Все товары этой категории станут неактивными.')) return;
        var id = $(this).data('id');
        $.ajax({
            url: '/api/categories/' + id,
            type: 'DELETE',
            success: function() {
                loadCategories();
            },
            error: function(xhr) {
                var err = xhr.responseJSON;
                $('#categoriesError').text(err && err.message ? err.message : 'Ошибка').show();
            }
        });
    });

    // Инициализация
    loadCategories();
});
