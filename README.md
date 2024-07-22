# pool-service

Api для организации работы бассейна

### Stack
  * Spring Boot 3.2.8
  * Java 17
  * Hibernate
  * Postgres
  * Docker

## Возможные действия:
  * Добавление клиентов
  * Редактирование клиентов
  * Получение данных о клиенте
  * Получение занятых записей на определенную дату
  * Получение доступных записей на определенную дату
  * Добавление запись клиента на определенное время
  * Отмена записи клиента на определенное время

## Запуск проекта
  Необходимо склонировать репозиторий:
  ```
     git clone github.com/sh1neqd/pool-service
     git checkout main
```
  Далее генерируем .jar file и запускаем проект с помощью docker-compose:
```
  mvn clean package  
  docker-compose build
  docker-compose up
```
  Запускаем Postman и теституем)
  
### API для работы с записями

#### 1. GET getAll (/api/v0/pool/timetable/all)

##### Описание
Получение занятых записей на определенную дату

##### Входные данные
```
    date: string [format = YYYY-MM-DD]
```
Например, http://localhost:8080/api/v0/pool/timetable/all?date=2024-07-24

##### Структура ответа
```
{[
    "time": string,
    "count": number //Количество занятых записей в указанное время
]}
```

#### 2. GET getAvailable (/api/v0/pool/timetable/available)

##### Описание
Получение доступных записей на определенную дату

##### Входные данные
```
    date: string
```

##### Выходные данные
```
{[
    "time": string,
    "count": number //Количество доступных записей в указанный время 
]}
```

#### 3. POST reserve (/api/v0/pool/timetable/reserve)

##### Описание
Добавить запись клиента на определенное время

##### Структура входных данных (body)
```
{
    "clientId": number,
    "date": string, [format = YYYY-MM-DD]
    "time": string  [format = HH:MM]
}
```


#### 4. GET cancel (/api/v0/pool/timetable/cancel)

##### Описание
Отмена записи клиента на определенное время

##### Структура входных данных (body)
```
{
    "orderId": number //Идентификатор записи
}
```
