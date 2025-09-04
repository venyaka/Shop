# Shop — интернет-магазин на Spring Boot

## Описание

Многофункциональный интернет-магазин с поддержкой регистрации, авторизации, ролей пользователей (USER/ADMIN), загрузкой изображений, админ-панелью, REST API и Swagger. Проект поддерживает деплой через Docker и автоматическое обновление на сервере через GitHub Actions.

---

## Основные возможности
- Регистрация и подтверждение email
- Авторизация с JWT (access/refresh токены, HttpOnly cookies)
- Роли пользователей: USER, ADMIN
- Админ-панель для управления товарами и категориями
- Загрузка изображений товаров
- REST API для работы с товарами, категориями, пользователями, файлами
- Swagger UI для тестирования API
- Защита маршрутов и ролей через Spring Security
- Адаптивный UI на Bootstrap + Thymeleaf
- CI/CD: автоматический деплой на сервер через GitHub Actions
- Docker: запуск приложения и базы данных в контейнерах

---

## Структура проекта
- `src/main/java` — исходный код (контроллеры, сервисы, фильтры, конфигурация)
- `src/main/resources/templates` — страницы на Thymeleaf
- `src/main/resources/static/js` — JS для фронта
- `docker-compose-file/docker-compose.yml` — Docker Compose для запуска
- `credentials-docker.env` — переменные окружения для Docker
- `.github/workflows/deploy.yml` — workflow для автоматического деплоя

---

## Быстрый старт (локально)
1. Установи JDK 17+ и Maven
2. Создай файл credentials-dev.env и подставь свои значения:

Пример `credentials-dev.env`:
```
SERVER_PORT=8184
BACKEND_PORT=8184:8184

POSTGRES_USER=postgres
POSTGRES_PASSWORD=12345
POSTGRES_DB=shop
POSTGRES_URL=jdbc:postgresql://db:5432/shop
POSTGRES_PORT=9877:5432

PGDATA=/var/lib/postgresql/data/pgdata
PGADMIN_DEFAULT_EMAIL=admin@admin.com
PGADMIN_DEFAULT_PASSWORD=root
PGADMIN_PORT=5050:80

SENDER_MAIL=<your-sender-mail>
SENDER_PASSWORD=<your-sender-password>
IPSTACK_ACCESS_KEY=<your-ipstack-acces-key>
JWT_SECRET=<your-jwt-secret>
```

3. Запусти приложение:
   ```
   ./mvnw spring-boot:run
   ```
4. Открой [http://localhost:8184](http://localhost:8184)

---

## Запуск через Docker
1. Установи Docker и Docker Compose
2. Проверь файл `credentials-docker.env` (доступы к БД, почте и т.д.)
3. Запусти:
   ```
   cd docker-compose-file
   docker compose --env-file ../credentials-docker.env up -d
   ```
4. Приложение будет доступно на порту, указанном в env-файле

---

## Автоматический деплой на сервер (CI/CD)
1. На сервере должен быть установлен Docker, Docker Compose, git, пользователь `deployuser` с SSH-ключом
2. В репозитории GitHub добавь секрет `HOST_SSH_PRIVATE_KEY` (приватный ключ для доступа к серверу)
3. Проверь, что проект склонирован на сервере в `/opt/app`
4. При пуше в ветку master workflow `.github/workflows/deploy.yml` автоматически обновит проект и перезапустит контейнеры

---

## Swagger
- Swagger UI доступен по адресу: `/swagger-ui.html` или `/swagger-ui/`
- Открыт для всех пользователей

---

## Основные эндпоинты
- `/api/authorize/register` — регистрация
- `/api/authorize/login` — авторизация
- `/api/authorize/verification` — подтверждение email
- `/api/products` — работа с товарами
- `/api/categories` — работа с категориями
- `/api/file` — загрузка и получение файлов
- `/api/users/info` — информация о текущем пользователе

---

## Защита маршрутов
- `/admin/**` — только для ADMIN
- `/profile`, `/products` — только для авторизованных
- `/swagger-ui/**`, `/v3/api-docs/**` — доступны всем

---

## Переменные окружения
Все секреты (доступы к БД, почте, JWT и др.) хранятся в файле `credentials-docker.env` (или `application.properties` для локального запуска).

---


## Авторы
- [Venyaka](https://github.com/venyaka)

---

Если нужна инструкция по настройке деплоя, SSH-ключей или переменных — смотри разделы выше или обратись к автору!

