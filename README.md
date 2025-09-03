# Shop � ��������-������� �� Spring Boot

## ��������

������������������� ��������-������� � ���������� �����������, �����������, ����� ������������� (USER/ADMIN), ��������� �����������, �����-�������, REST API � Swagger. ������ ������������ ������ ����� Docker � �������������� ���������� �� ������� ����� GitHub Actions.

---

## �������� �����������
- ����������� � ������������� email
- ����������� � JWT (access/refresh ������, HttpOnly cookies)
- ���� �������������: USER, ADMIN
- �����-������ ��� ���������� �������� � �����������
- �������� ����������� �������
- REST API ��� ������ � ��������, �����������, ��������������, �������
- Swagger UI ��� ������������ API
- ������ ��������� � ����� ����� Spring Security
- ���������� UI �� Bootstrap + Thymeleaf
- CI/CD: �������������� ������ �� ������ ����� GitHub Actions
- Docker: ������ ���������� � ���� ������ � �����������

---

## ��������� �������
- `src/main/java` � �������� ��� (�����������, �������, �������, ������������)
- `src/main/resources/templates` � �������� �� Thymeleaf
- `src/main/resources/static/js` � JS ��� ������
- `docker-compose-file/docker-compose.yml` � Docker Compose ��� �������
- `credentials-docker.env` � ���������� ��������� ��� Docker
- `.github/workflows/deploy.yml` � workflow ��� ��������������� ������

---

## ������� ����� (��������)
1. �������� JDK 17+ � Maven
2. ������ ���� `src/main/resources/application.properties` � ������� ����������� (��� ��������� `credentials-dev.env`)
3. ������� ����������:
   ```
   ./mvnw spring-boot:run
   ```
4. ������ [http://localhost:8184](http://localhost:8184)

---

## ������ ����� Docker
1. �������� Docker � Docker Compose
2. ������� ���� `credentials-docker.env` (������� � ��, ����� � �.�.)
3. �������:
   ```
   cd docker-compose-file
   docker compose --env-file ../credentials-docker.env up -d
   ```
4. ���������� ����� �������� �� �����, ��������� � env-�����

---

## �������������� ������ �� ������ (CI/CD)
1. �� ������� ������ ���� ���������� Docker, Docker Compose, git, ������������ `deployuser` � SSH-������
2. � ����������� GitHub ������ ������ `HOST_SSH_PRIVATE_KEY` (��������� ���� ��� ������� � �������)
3. �������, ��� ������ ����������� �� ������� � `/opt/app`
4. ��� ���� � ����� master workflow `.github/workflows/deploy.yml` ������������� ������� ������ � ������������ ����������

---

## Swagger
- Swagger UI �������� �� ������: `/swagger-ui.html` ��� `/swagger-ui/`
- ������ ��� ���� �������������

---

## �������� ���������
- `/api/authorize/register` � �����������
- `/api/authorize/login` � �����������
- `/api/authorize/verification` � ������������� email
- `/api/products` � ������ � ��������
- `/api/categories` � ������ � �����������
- `/api/file` � �������� � ��������� ������
- `/api/users/info` � ���������� � ������� ������������

---

## ������ ���������
- `/admin/**` � ������ ��� ADMIN
- `/profile`, `/products` � ������ ��� ��������������
- `/swagger-ui/**`, `/v3/api-docs/**` � �������� ����

---

## ���������� ���������
��� ������� (������� � ��, �����, JWT � ��.) �������� � ����� `credentials-docker.env` (��� `application.properties` ��� ���������� �������).

---


## ������
- [Venyaka](https://github.com/venyaka)

---

���� ����� ���������� �� ��������� ������, SSH-������ ��� ���������� � ������ ������� ���� ��� �������� � ������!

