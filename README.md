# Scheduler as a Service

Scheduler as a Service is a project that allows managing external scheduled
jobs for your application.

The idea is to have UI components and an administrator who is responsible for
creating credentials for external apps.

After creating the credentials, an external user (app) can generate an admin token,
which is used only for obtaining an additional access (short-lived) token.

Short-lived tokens are intended to be used for managing the scheduled jobs by the external users.

## Development Phases

For developing this project, I'm using (as the only contributor) the following tech stack:

- Java 21 / Spring Boot
- JPA & Hibernate
- PostgreSQL database
- Spring Quartz Scheduler

Below, you can find the project roadmap (development phases).

### Phase I (in progress):

- ~~Create project structure~~
- ~~Create endpoints for creating and activating an external user/app (only for admins)~~
- ~~Create endpoints for generating an Admin Token for a specific user/app~~
- ~~Implement basic security rules~~
- Create endpoint for generating temporary access token (JWT)

### Phase II:

- Create scheduled jobs using REST API
- Implement "force run" for scheduled jobs using REST API
- Use Spring Quartz Scheduler for managing scheduled jobs
- Implement JWT Authentication Filter

### Phase III:

- Cover the app with unit/integration tests
- Develop the User Interface

### Phase IV:

- Test the application
- Support multiple deployments (+ Cloud)
- Find someone who would like to use this tool

## Contribution/Suggestions

If someone is interested in contributing or has some suggestions,
please contact me via email at hedzaprog@gmail.com.

## Author

Heril Muratović  
Software Engineer  
<br>
**Mobile**: +38269657962  
**E-mail**: hedzaprog@gmail.com  
**Skype**: hedza06  
**Twitter**: hedzakirk  
**LinkedIn**: [Heril Muratović](https://www.linkedin.com/in/heril-muratovi%C4%87-021097132/)  
**StackOverflow**: [Heril Muratovic](https://stackoverflow.com/users/4078505/heril-muratovic)
