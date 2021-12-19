# quarkus-proj3-reactive-sql Project

A Sample Java MicroProfile Microservice Application in RedHat Quarkus deomnstrating Reactive SQL. Deomstratin Quarkus's Reactive style, in addition to Imperative Style of Programming.
https://quarkus.io/guides/quarkus-reactive-architecture

Quarkus employes "Mutiny", a Intuitive Event-Driven Reactive Programming Library for Java.
https://smallrye.io/smallrye-mutiny/

Uses Prostgresql to demonstrate:

docker run --name my_reactive_db -e POSTGRES_USER=user -e POSTGRES_PASSWORD=password -e POSTGRES_DB=my_db -p 5432:5432 postgres.

docker start my_reactive_db

![This is an image](https://github.com/pranavnayak/quarkus-proj3-reactive-sql/blob/main/Capture.JPG)
