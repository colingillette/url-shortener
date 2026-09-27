# URL Shortener

An API that accepts custom URLs and returns an associated longform counterpart.

## Run Locally

1. Open Docker
2. Run postrges image
   - Ports: Add 5432 in the field
   - Volumes: pgdata:/var/lib/postgresql/data
   - Variable: POSTGRES_PASSWORD: <password>
3. `docker ps` to get the container name
4. `docker exec -it <container_name> psql -U postgres` to access the database
5. \l to list databases
6. \c urlshortener to connect to the database
   - Use ddl.sql if the database is lost

## Database Setup

1. Create Site and Hit tables
2. Add trigger to update revision timestamp on update of Site table
3. Create index on site_id within Hit table.

## Coming Soon

- [ ] Site Controller
  - [X] Save a new entry
  - [ ] Delete an existing entry
  - [X] Edit an existing entry
  - [X] Retrieve an existing entry
- [ ] Hit Controller
  - [X] Log a hit for an existing entry
  - [X] Redirect to the longform URL
- [ ] Generate short URL if one is not provided
- [ ] Create an error page for invalid short URLs
- [X] Postgres database connection
- [ ] Hosted on AWS
