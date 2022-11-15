
### App info

The app fetches top 100 celebrities from https://www.imdb.com/list/ls052283250/
and saves into mongo database with the following information:
* Name
* Date of birth
* Gender
* List of jobs
* Picture

The code and the DB should run on your computer.

The application has 2 endpoints:
* GET /api/v1/actors/top-100-celebrities?job={job} - fetch top 100 celebrities actors from DB by job
* POST /api/v1/actors/reset-top-100-celebrities - drop top 100 celebrities from local DB and load from IMDB into local DB

### TODO list
* Additional logic (source of data) for gender recognition. Currently it is done by using job title.
* Add database schema migration engine and create indexes and scheme changes through migrations.
* Add unit tests.
* Add spring boot integration tests.
* Add Open API swagger documentation for endpoints.
* Dockerize application.
* Update global exception handler to not expose internal error messages (for example for 500 status error codes).
* To further increase loading speed from IMDB we may find API to use instead of web page parsing.
* We may change application to use reactive client to use less threads while awaiting response.