CREATE TABLE companies
(
    id                     VARCHAR PRIMARY KEY,
    name                   VARCHAR NOT NULL,
    industry               VARCHAR NOT NULL,
    location_address       VARCHAR NOT NULL,
    location_post_code     VARCHAR NOT NULL,
    location_city          VARCHAR NOT NULL,
    location_country       VARCHAR NOT NULL,
    founded_year           INTEGER NOT NULL,
    website                VARCHAR,
    email                  VARCHAR,
    phone                  VARCHAR,
    social_media_facebook  VARCHAR,
    social_media_instagram VARCHAR,
    social_media_linked_in VARCHAR
);

CREATE TABLE employees
(
    id         VARCHAR PRIMARY KEY,
    first_name VARCHAR                  NOT NULL,
    last_Name  VARCHAR                  NOT NULL,
    email      VARCHAR                  NOT NULL,
    phone      VARCHAR,
    position   VARCHAR                  NOT NULL,
    department VARCHAR                  NOT NULL,
    start_date TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE projects
(
    id          VARCHAR PRIMARY KEY,
    name        VARCHAR                  NOT NULL,
    description VARCHAR                  NOT NULL,
    startDate   TIMESTAMP WITH TIME ZONE NOT NULL,
    endDate     TIMESTAMP WITH TIME ZONE NOT NULL,
    status      VARCHAR,
    budget      NUMERIC(16, 2)
);
