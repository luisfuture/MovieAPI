# MovieAPI
Movie API, it is an exercise with spring boot, Jpa and h2 database

#Proyecto de Aplicación de Conceptos

Crear una API RESTful que modele un catálogo de películas que contenga un título, año, estudio de grabación, director y categoría (horror, acción, comedia, etc.) para cada película.

Se debe poder listar películas, obtener una película por ID, crear una película, modificar una película y eliminar una película.

Adicionalmente se debe poder ingresar ratings por película (un rating es una valoración entre 1 y 5 estrellas).

Cuando se obtenga o se liste una película, la respuesta debe incluir el promedio de ratings correspondientes a la o las películas.

Al listar películas se debe poder ordenar por título ascendentemente o por rating descendentemente. Además, se debe poder listar los ratings de una película en específico.
No se requiere ningún tipo de autenticación para utilizar el API.


# API Movie [ Docs API:http://localhost:9000/swagger-ui.html#/ , API version: 1.0 ]

GET /categories Get a paginated list of all categories.

GET /categories/{categoryId} Get a single category.

GET /categories/{categoryId}/movies Get a paginated list of all movies of the category

GET /movies Get a paginated list of all movies.

POST /movies Create a movie resource.

DELETE /movies/{movieId} Delete a Movie resource.

GET /movies/{movieId} Get a single Movie.

PUT /movies/{movieId} Update a Movie resource.

GET /movies/{movieId}/ratings Get a paginated list of all ratings.

POST /movies/{movieId}/ratings Create a rating resource.

DELETE /movies/{movieId}/ratings/{ratingId} Delete a rating resource.

GET /movies/{movieId}/ratings/{ratingId} Get a single Rating.

PUT /movies/{movieId}/ratings/{ratingId} Update a rating resource.

#Configuration
resources/application.properties

server.port: 9000 

management.port: 9001

management.address: 127.0.0.1

#Data Configuration
resources/Data.sql

resoruces/messages/messages.properties


