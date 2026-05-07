# Contest Platform
![CI](https://github.com/MaksimKosarevHSE/Contest-Platform/actions/workflows/ci.yml/badge.svg)

## Обзор
Этот проект — микросервисная система для проведения контестов и решения задач по программированию с поддержкой горизонтального и вертикального масштабирования. Она позволяет проводить контесты, управлять задачами, принимать и тестировать решения участников.

## Архитектура

Диаграмма архитектуры микросервисов представлена в MIRO.

[Ссылка на диаграмму в MIRO](https://miro.com/app/board/uXjVGy4zEVE=/?share_link_id=581367335863)

Основные микросервисы / компоненты:

- **API Gateway** — маршрутизирует запросы
- **Auth Service** — отвечает за аутентификацию
- **Problem Service** — хранит задачи, контесты и прогресс участников
- **Submission Service** — обрабатывает посылки участников, управляет процессом тестирования и публикует результаты
- **Judging Service** — тестирует посылки участников

Все сервисы используют **PostgreSQL** в качестве основной базы данных, **Redis** для кэширования (таблицы лидеров, прогресс по задачам, refresh токены) и **Kafka** для асинхронного общения микросервисов. Миграции схем БД управляются через **Liquibase**.

## Пайплайн обработки посылки

1. Пользователь отправляет запрос через `gateway`, где токен валидируется через `auth-service`.
2. `submission-service` принимает решение и запрашивает у `problem-service` ограничения задачи и данные контеста.
3. Решение сохраняется в БД, после чего событие о новой отправке публикуется в Kafka через outbox.
4. `judging-service` получает событие, компилирует решение, запускает его на тестах и формирует verdict.
5. Verdict отправляется обратно через Kafka в `submission-service`, где обновляется статус решения.
6. Если решение относится к контесту, `submission-service` публикует событие обновления standings.
7. `problem-service` обрабатывает это событие, обновляет таблицу результатов и кэш leaderboard в Redis.
8. Пользователь получает актуальный статус решения и обновленные standings через API.

## Технологический стек
- **Java 21** — язык
- **Spring Boot** — основа микросервисов
- **Spring Cloud Gateway** — маршрутизация запросов
- **Spring Security** — безопасность
- **JJWT** — работа с токенами 
- **Spring Data JPA / Hibernate** — ORM, общение с БД
- **Liquibase** — миграции схем БД
- **Apache Kafka** — брокер сообщений
- **Redis** — кэш, подсчет таблиц лидеров, хранилище токенов
- **PostgreSQL** — реляционная база данных
- **Spring Messaging / WebSocket** — уведомления
- **MapStruct** — маппинг DTO и моделей
- **Lombok** — сокращение boilerplate-кода
- **SpringDoc OpenAPI (Swagger UI)** — документация API
- **Docker / Docker Compose** — развертывание инфраструктуры и запуск сервисов
- **Maven** — сборка и управление зависимостями
  
## Структура проекта

```text
ContestSystem/
├── auth-service/               # Аутентификация, регистрация, JWT
├── gateway/                    # Единая точка входа, прокидывание user context
├── common/                     # Общие DTO, events, enum'ы и контракты
├── problem-contest-service/    # Задачи, контесты, регистрация на контест, таблицы лидеров
├── submission-service/         # Прием решений, хранение посылок, websocket-уведомления
├── judging-service/            # Компиляция, запуск решений, проверка на тестах, отправка вердиктов
├── parent/                     # Общий parent pom для модулей
├── docker-compose-dev.yaml     # Запуск проекта и инфраструктуры на локалке
├── build.ps1                   # Сборка проекта
└── run-all.ps1                 # Запуск сервисов
```
## Сборка и запуск

1. Собрать все сервисы:

```bash
mvn -s .mvn/settings.xml -B clean package -DskipTests
```

2. Запустить сервисы и инфраструктуру в Docker:

```bash
docker compose -f docker-compose-dev.yaml up --build
```

## Swagger UI
- [Auth Service](http://localhost:8003/swagger-ui.html)
- [Problem Service](http://localhost:8000/swagger-ui.html)
- [Submission Service](http://localhost:8001/swagger-ui.html)

## Ближайшие апдейты
- Дополнить функционал
- Покрыть сервисы тестами
- Grafana
- Использование gRPC между сервисами

## Лицензия

Проект распространяется под лицензией MIT. Подробнее см. в файле [LICENSE](LICENSE).
