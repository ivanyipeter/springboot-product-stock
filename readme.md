Raktárkészlet kezelése

1. REST hozzáadása
    - Spring Web Starter függőség hozzáadása
    - Ping REST controller létrehozása
        - GET /ping
        - Válasz: 
            - Content-Type: text/plain
            - Body: Pong
1. Product CRUD api létrehozása
    - JPA függőségek
    - MySQL config
    - Product entitás létrehozása
        - id: int
        - name: String
        - description: String
        - count: int
    - **C**reate, **R**ead, **U**pdate, **D**elete REST végpontok
        - a pathVariable mindig id legyen
        - U: Patch, Post
            - https://www.restapitutorial.com/lessons/httpmethods.html
        - Listázás is legyen
        - Authentikáció nélkül
        - Tesztelés: pl. Postman, vagy IntelliJ Test restful webservice tool
    - Validáció
        - 400, Bad Request
        - Body: validációs hibák (property, error)
1. Authentikáció/Authorizáció
    - Security Starter
    - Először Http Basic auth
    - JWT elmélet (mentor segítség)
    - JWT authentikáció használata
    - Permission based authorization
1. ProductStock CRUD Rest
    - Productok a Stockban probléma kezelése
1. Interceptorok/Filterek/Exception kezelés
1. Tesztelés
    - Unit tesztek
    - mockmvc integrácós tesztek
