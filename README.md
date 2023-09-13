## Load testing of GraphQL API

This is a project used for talk "Load testing made easy with Gatling".

There are three submodules in this project:

* `server` - sample application for which load tests are written,
  using [Grackle](https://github.com/gemini-hlsw/gsp-graphql)
* `load-tests` - load tests (using [Gatling](https://gatling.io/)), testing sample application (server)
* `test-data` - simple util to help populate database with some noticeable amount of data,
  using [Mockaroo](https://mockaroo.com/) service

## Prerequisites

### Start local database

Start local database (PostgreSQL) in docker (it will be started in the background):

```bash
./start-local-docker-compose.sh
```

Run migrations:

```bash
sbt flywayMigrate
```

### Load data into database

You need to get API key for [Mockaroo](https://www.mockaroo.com/) (free version) and put that as environment
variable (`MOCKAROO_API_KEY`) or create file `.env.sh` with it:

```
#!/bin/bash

export MOCKAROO_API_KEY=<put_your_api_key_here>
```

There is a public dataset that this project is using: https://mockaroo.com/e11c4970

To populate the database with 500 companies (using dataset from Mockaroo) you can invoke below sbt run command:

```bash
sbt test-data/run
```

**Note**: There is no need to run migrations and populate the local database every time you start it, docker-compose
file defines volume for PostrgreSQL, so data will be available there (until you intentionally remove docker volume).

## Run the server

```bash
sbt server/run
```

## Invoke load tests

Invoke all tests (a.k.a. Gatling simulations):

```bash
sbt "load-tests/Gatling/test"
```

Invoke test for query company by id:

```bash
sbt "load-tests/Gatling/testOnly io.github.rpiotrow.simulations.CompanyGraphQLQuerySimulation"
```

Invoke test for query company list:

```bash
sbt "load-tests/Gatling/testOnly io.github.rpiotrow.simulations.CompaniesGraphQLQuerySimulation"
```

## Stop local database (clean up)

Stop local database:

```bash
./stop-local-docker-compose.sh
```
