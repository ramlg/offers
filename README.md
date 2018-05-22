# offers

This is a readme file for coding exercies. 

The exercise is mainly to create a Restful API for creating and getting the Offers for a merchant.

# Assumptions

I have made few assumption to keep the implementation a simple Restful API. Here are the assumptions made.

1. The storage used for this example is a in memory Hashmap to store the offers against the offerId as key.
2. The offer object defined has the minimum required property and it is assumed that the offer will be valid from the time it is created until expires.
3. Merchant would provide the endDate for the offer, and not the duration from the time it is created. As it is easier to manage the time until the offer is valid by providing the endDate than the duration from the time offer become Live.
4. No validation is added to the offer object to keep the exercise restricted to simple RESTful exercise.

# Implementation

The implementaion is done using Spring Boot framework, as Spring Boot provide a built in server to run the application. Below is a list of REST api provided by application.

The application can be started using command below : 
```
./mvnw spring-boot:run
```
You should have maven and java8 installed and corresponding MVN_HOME and JAVA_HOME environment set to run this command. Once run application will start and will listen on port 8080. 

Postman can be used to run the manual tests.


## Create an Offer
URL: http://localhost:8080/offers

HTTP Method: POST

### Example Request:
```
POST /offers HTTP/1.1
Host: localhost:8080
Content-Type: application/json
{
	"description" : "Best Buy offer",
	"currency"	: "GBP",
	"price"		: "50.50",
	"endDate"	: "2018-05-28T00:00:00Z"
}
```
### Response:

1. For a succesful request a HTTP Status of 201 Created with Location header value set to the URL for the new offer.This is how the Location Header will look like:
location: http://localhost:8080/offers/5e1a48f9-64ae-4d76-bda1-3b70b74d6cd8
2. For a request containing an Id and the offerId exists, an HTTP status of 409 Conflict will be returned.

## Get Offer details
URL: http://localhost:8080/offers/{offerId}
HTTP Method: GET

### Example Request:
```
GET /offers/5e1a48f9-64ae-4d76-bda1-3b70b74d6cd8 HTTP/1.1
Host: localhost:8080
```
### Response:

1. For a succesful request a HTTP Status of 200 Ok with Body containg the json offer object would be returned. Below is the example response for a successful request.
```
{
    "id": "c30bdb85-0e6b-4d7e-a8e4-0f788ceebb12",
    "description": "Best Buy offer",
    "currency": "GBP",
    "price": 50.5,
    "endDate": "2018-05-28T00:00:00Z",
    "status": "Live"
}
```
2. A Http Status 404 Not Found will be returned when no offer for the given offer id is found.
3. A Http Status of 400 Bad Request will be returned when the Id is not in the UUID format.

## Get All Offers
URL: http://localhost:8080/offers
HTTP Method: GET

### Example Request:
```
GET /offers HTTP/1.1
Host: localhost:8080
```
### Response:

1. For a succesful request a HTTP Status of 200 Ok with Body containg the json offer object would be returned. Below is the example response for a successful request.
```
[
    {
        "id": "f6760bfe-9187-4be0-b0cf-b01e9b6f9507",
        "description": "Best Buy offer",
        "currency": "GBP",
        "price": 50.5,
        "endDate": "2018-05-28T00:00:00Z",
        "status": "Live"
    },
    {
        "id": "bca164dc-6b07-4344-84c2-74faaf47625d",
        "description": "Best Buy offer",
        "currency": "GBP",
        "price": 50.5,
        "endDate": "2018-05-21T00:00:00Z",
        "status": "Expired"
    },
    {
        "id": "392c6a85-98d4-46bb-b62b-149107713a60",
        "description": "Best Buy offer",
        "currency": "GBP",
        "price": 50.5,
        "endDate": "2018-05-28T00:00:00Z",
        "status": "Cancelled"
    }
]
```

## Cancel an Offer
URL: http://localhost:8080/offers/{offerId}
HTTP Method: PATCH

To cancel an offer a HTTP PATCH method is chosen, as PUT method is to update or replace the whole offer details. and Need the whole offer object to be sent for PUT method. while in PATCH method an instruction can be sent to cancel the offer. Once cancelled, when the offer is queried again. The status of the offer will be returned as `Cancelled`.

### Example Request:
```
PATCH /offers/392c6a85-98d4-46bb-b62b-149107713a60 HTTP/1.1
Host: localhost:8080
Content-Type: application/json
{
	"command" : "cancel"
}
```

### Response:

1. For a succesful request a HTTP Status of 200 Ok will be returend. When the offer is queried again using the offerId the response will show status as `Cancelled`. Below is the example response

```
{
    "id": "392c6a85-98d4-46bb-b62b-149107713a60",
    "description": "Best Buy offer",
    "currency": "GBP",
    "price": 50.5,
    "endDate": "2018-05-28T00:00:00Z",
    "status": "Cancelled"
}
```

2. when wrong command is passed than a 400 Bad Request would be returned.
3. When offer does not exists than a 404 Not Found would be returned.
