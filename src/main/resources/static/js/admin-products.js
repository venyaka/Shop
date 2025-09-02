$(function() {
    // Загрузка категорий для селектора
    function loadCategories() {
        $.ajax({
            url: '/api/categories',
            type: 'GET',
            success: function(data) {
                var select = $('#productCategory');
                select.empty();
                data.forEach(function(cat) {
                    select.append('<option value="' + cat.id + '">' + cat.name + '</option>');
                });
            }
        });
    }

    // Загрузка товаров
    function loadProducts() {
        $.ajax({
            url: '/api/products',
            type: 'GET',
            success: function(data) {
                var tbody = $('#adminProductsList');
                tbody.empty();
                data.forEach(function(prod) {
                    var status = prod.isActive ? 'Активен' : 'Не активен';
                    tbody.append('<tr>' +
                        '<td>' + prod.id + '</td>' +
                        '<td>' + prod.name + '</td>' +
                        '<td>' + prod.price + '</td>' +
                        '<td>' + (prod.categoryName || '-') + '</td>' +
                        '<td>' + status + '</td>' +
                        '<td>' +
                            '<button class="btn btn-sm btn-primary me-1 editProductBtn" data-id="' + prod.id + '">Редактировать</button>' +
                            '<button class="btn btn-sm btn-danger deleteProductBtn" data-id="' + prod.id + '">Удалить</button>' +
                        '</td>' +
                    '</tr>');
                });
            },
            error: function(xhr) {
                var err = xhr.responseJSON;
                $('#productsError').text(err && err.message ? err.message : 'Ошибка').show();
            }
        });
    }

    // Открытие модального окна для создания товара
    $('#createProductBtn').click(function() {
        $('#productForm')[0].reset();
        $('#productId').val('');
        $('#productModal').modal('show');
        loadCategories();
    });

    // Открытие модального окна для редактирования товара
    $(document).on('click', '.editProductBtn', function() {
        var id = $(this).data('id');
        $.ajax({
            url: '/api/products',
            type: 'GET',
            success: function(data) {
                var prod = data.find(function(p) { return p.id === id; });
                if (prod) {
                    $('#productId').val(prod.id);
                    $('#productName').val(prod.name);
                    $('#productDescription').val(prod.description);
                    $('#productPrice').val(prod.price);
                    $('#productCategory').val(prod.categoryId);
                    $('#productStatus').val(prod.isActive ? 'true' : 'false');
                    $('#productModal').modal('show');
                    loadCategories();
                }
            }
        });
    });

    // Сохранение товара (создание/редактирование)
    $('#productForm').submit(function(e) {
        e.preventDefault();
        var id = $('#productId').val();
        var url = id ? '/api/products/' + id : '/api/products';
        var method = id ? 'PUT' : 'POST';

        var formData = new FormData();
        if (id) formData.append('id', id);
        formData.append('name', $('#productName').val());
        formData.append('description', $('#productDescription').val());
        formData.append('price', $('#productPrice').val());
        formData.append('categoryId', $('#productCategory').val());
        formData.append('isActive', $('#productStatus').val());
        var imageInput = $('#productImage')[0];
        if (imageInput && imageInput.files && imageInput.files[0]) {
            formData.append('image', imageInput.files[0]);
        }

        $.ajax({
            url: url,
            type: method,
            data: formData,
            processData: false,
            contentType: false,
            success: function() {
                $('#productModal').modal('hide');
                loadProducts();
            },
            error: function(xhr) {
                var err = xhr.responseJSON;
                $('#productFormError').text(err && err.message ? err.message : 'Ошибка').show();
            }
        });
    });

    // Удаление товара
    $(document).on('click', '.deleteProductBtn', function() {
        if (!confirm('Удалить товар?')) return;
        var id = $(this).data('id');
        $.ajax({
            url: '/api/products/' + id,
            type: 'DELETE',
            success: function() {
                loadProducts();
            },
            error: function(xhr) {
                var err = xhr.responseJSON;
                $('#productsError').text(err && err.message ? err.message : 'Ошибка').show();
            }
        });
    });

    // Инициализация
    loadProducts();
    loadCategories();
});
