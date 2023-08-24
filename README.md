### tracking-mail-items  
Бэк-сервис отслеживания почтовых отправлений.  

**Возможности:** 
* Операции с отправлением, выполняемые почтовым отделением: REGISTER, ARRIVE, DEPART, HAND_OVER;
* Возможность просматривать список отправлений с сортировкой по выбранному полю. Используется KeySet пагинация.
* Возможность просматривать трэк отправления с сортировкой по выбранному полю. Фильтрация по типу отправления (LETTER, 
PARCEL, WRAPPER, POSTCARD), по списку id, по списку почтовых индексов, по диапазону даты операции. Можно указывать 
одновременно все фильтры или выборочно. Используется KeySet пагинация.

**Стек:**
- Java 11
- Spring Boot
- Hibernate
- PostgresSql, H2
- Junit
- Mockito
- Maven

### Open API
- swagger-ui
    - OpenAPI definition: localhost:8080/docs/swagger-ui.html
    - OpenAPI Docs: localhost:8080/docs
- specification
    - [Specification.json](Specification.json)

**Запуск**
* Собрать проект командой: `mvn clean package`
* Запустить из каталога, содержащего собранный tracking-mail-items-0.0.1-SNAPSHOT.war:
