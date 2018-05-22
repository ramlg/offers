# offers

This is a readme file for coding exercies. 

The exercise is mainly to create a Restful API for creating and getting the Offers for a merchant.

# Assumptions

I have made few assumption to keep the implementation a simple Restful API. Here are the assumptions made.

1. The storage used for this example is a in memory Hashmap to store the offers against the offerId as key.
2. The offer object defined has the minimum required property and it is assumed that the offer will be valid from the time it is created until expires.
3. Merchant would provide the endDate for the offer, and not the duration from the time it is created. As it is easier to manage the time until the offer is valid by providing the endDate than the duration from the time offer become Live.
4. No validation is added to the offer provided to keep the exercise restricted to simple RESTful exercise.

# Implementation

The implementaion is done using Spring Boot framework, as Spring Boot provide a built in server to run the application. Below is a list of REST api provided by application.

## Create an Offer
URL: http://localhost:8080/offers
HTTP Method: POST
Example Request:

{
  ""
}
Create an offer is implemented using the Http POST method.
