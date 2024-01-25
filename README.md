##Info
I've used JDK 21, Spring boot 3, Postgres, RabbitMq to resolve this task.
I made two implementations of PhoneService, one works with postgres, the second stores everything in memory you can switch between them using storage.db flag in application.properties file.

I've decided to add an ID field for each phone. This field will be used to book/return phone. Since postgres adds an index on PK by default, the selection will be carried out quickly. It was also possible to add an index to the NAME field if you needed functionality with a search by name.
Also I've added extra checking bookedBy name for return function.

## How to run:
    1 . run command: docker compose -f env/docker-compose.yml -p mobile-share up (this command will run postgres DB and
    RabbitMq in docker)
    2. run com.task.mobileshare.MobileShareApplication class (also I've added Docker file you can run this project in Docker)

## How to test:
    run mvn test

## OpenUI link:
http://localhost:8080/swagger-ui/index.html

## What aspect of this exercise did you find most interesting?
The most interesting was to resolve race conditions.
For ***InMemoryPhoneService*** I've used ConcurrentHashMap class to save all data. ConcurrentHashMap dividing complete hashtable array into segments or portions and allowing parallel access to those segments. I've found this really useful.
For ***DataBasePhoneServiceImpl*** I've used @Transactional annotation in repository class. This helps to prevent race conditions because the database operations (checking availability and updating the phone's status) are executed atomically. If two users try to book the same phone simultaneously, the database transaction ensures that only one of them will be able to successfully complete the booking operation.

## What did you find most cumbersome?
The task has several solutions, it was difficult to choose which of these solutions to implement.