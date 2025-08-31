package veniamin.shop.backend.exception.errors;

public enum NotFoundError {
    FILE_NOT_FOUND("Файл не найден"),

    USER_NOT_FOUND("Пользователь не был найден");

    private String message;

    NotFoundError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
