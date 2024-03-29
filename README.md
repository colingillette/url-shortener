# URL Shortener

An API that accepts custom URLs and returns an associated longform counterpart.

## Run Locally

Comes with a basic H2 connection and a `Seeder` class, which will populate the database on app startup.

## Coming Soon

- [ ] Fix improper use of internal server error
- [ ] Admin Controller
  - [x] Save a new entry
  - [ ] Edit an existing entry
  - [ ] Delete an existing entry
  - [ ] Authentication
- [ ] Optional require password for redirect
  - [ ] Admin Configuration Endpoint
- [ ] Upgrade Admin validation for real inputs
- [ ] Link expiration and expiration job
- [ ] Generate short URL if one is not provided
- [ ] Save state on app shutdown in H2
- [ ] MySql database connection
- [ ] Hosted on AWS
