# car-parking

## Overview
Spring boot application to simulate autonomous parking process

## Guidelines
1. Clone this repository
2. Go to the repository folder ('/bidding-system') and run the following commands:
3. Run the Spring boot
```
./gradlew bootRun
```
4. The application has three endpoints:
4.1 Assign car to parking slot
```
curl --location --request POST 'http://localhost:8080/parking' \
--header 'Content-Type: application/json' \
--data-raw '{
    "weight": 900,
    "height": 1700
}'
```
4.2 Create invoice
```
curl --location -g --request POST 'http://localhost:8080/parking/{slotId}/create-invoice'
```
4.3 Get all assigned parking slots:
```
curl --location --request GET 'http://localhost:8080/parking/'
```




