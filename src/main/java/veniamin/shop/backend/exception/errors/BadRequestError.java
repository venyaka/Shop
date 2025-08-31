package veniamin.shop.backend.exception.errors;

public enum BadRequestError {

    NOT_CORRECT_PASSWORD("Неверный пароль"),
    NOT_CORRECT_REFRESH_TOKEN("Неверный рефреш токен"),
    USER_ALREADY_VERIFICATED("Пользователь уже был верифицирован"),
    USER_NOT_VERIFICATED("Пользователь не верифицирован"),
    NOT_CORRECT_VERIFICATION_CODE("Код верификации не корректен или не был запрошен"),
    USER_ALREADY_EXISTS("Пользователь с такой почтой уже существует"),

    NOT_CORRECT_FILE_EXTENSION_FOR_THIS_FILE_TYPE("Неверное расширения для данного типа файлов"),
    FILE_CAN_NOT_BE_DELETED("Файл не может быть удален"),
    FILE_DELETED("Файл удален"),
    FILE_MUST_BE_IMAGE("Файл должен быть изображением"),
    NOT_ALLOWED_EXTENSION("Данное расширение не является допустимым");

    private String message;

    BadRequestError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
