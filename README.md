# REST API приложения на Spring Boot с подключением к PostgreSQL. 

Проект включает в себя:
- Сущность Person (id, name, email)
- Репозиторий на Spring Data JPA
- REST контроллер с CRUD операциями
- Конфигурацию подключения к БД
- Dockerfile для сборки образа приложения
- Docker Compose для запуска приложения и PostgreSQL


## Сборка и запуск контейнеров:
```bash
docker-compose up -d --build
```

Приложение будет доступно по адресу:
http://localhost:8080

## CRUD операции:

- Swagger
http://localhost:8080/swagger-ui/index.html

- Создание записи:

```bash
curl -X POST http://localhost:8080/api/persons \
     -H "Content-Type: application/json" \
     -d '{"name":"John Doe","email":"john@example.com"}'
```

- олучение всех записей:

```bash
curl http://localhost:8080/api/persons
```

- Обновление:

```bash
curl -X PUT http://localhost:8080/api/persons/1 \
     -H "Content-Type: application/json" \
     -d '{"name":"Jane Doe","email":"jane@example.com"}'
```

- Удаление:
```bash
curl -X DELETE http://localhost:8080/api/persons/1
```

## Остановка контейнеров
```bash
docker-compose down 
```



## pgsql
```sql
 SELECT * FROM persons LIMIT 5;
```