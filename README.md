# geekday-eventsourcing
1. To build use
  ./gradlew clean build shadowJar
  
2. To run 
   java -jar build/libs/geekday-eventsourcing-1.0-all.jar
   
3. Test it with 
  curl --data "name=name&address=address" -i http://localhost:8080/customer
  It should return
  
  HTTP/1.1 201 Created
  Date: Sat, 16 Jul 2016 05:36:02 GMT
  Location: http://localhost:8080/profile
  Content-Length: 0
  Server: Jetty(9.4.z-SNAPSHOT
  
  Use Cases
  
1. Account and Customer

Customer registers.
Account is notified.
Account is Created. Customer should be updated with account id.

Account is updated.

2. Flight is searched to get flight availability in inventory.
Customer books a flight.
Tickets are issued once 

Flight search service should give tickets are not issued.
