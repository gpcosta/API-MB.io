# MB.io - SINFO Test Drive Challenge API (backend)

The challenge was to build an API that gives us some info about the possibilities of making a test-drive in some dealerships.
I made it using Java 9.0.4, Maven 3.5.2 and Spring Boot (1.5.10.RELEASE - see pom.xml)

## Dependencies

Spring Book;<br>
JSON simple: https://mvnrepository.com/artifact/com.googlecode.json-simple/json-simple;<br>
JUnit: https://mvnrepository.com/artifact/junit/junit

## How to build and run my application

* After all setup (install Java and Maven), open command line on Windows or konsole in Linux and position yourself in API_MB/app
* Then type: `mvn spring-boot:run (-Dfile=path/to/json)`<br>
<i>Note: the file parameter is optional. If you want to start with the "dataset.json" default, there is no need to add this parameter.</i>
* And voilá, the API is running

## How to run my tests

* Open command line on Windows or konsole in Linux and position yourself in API_MB/app
* Then type: `mvn test`

## API documentation

### Response

All the responses have a 'result', a 'code' and a 'status'.

<b>Result</b> - this is the field that will have the result you asked for

<b>Code</b> - will have an HTTP code. It means that if you received a <300 code, doesn't occur any error.
    All codes possible in this API:<br>
    - [200] - Everything is fine<br>
    - [201] - An object was created and you will receive it<br>
    - [204] - The search that you made doesn't have any result<br>
    - [400] - A bad request was made<br>
    - [404] - Something that you tried to access was not found<br>
    - [500] - Something unexpected happened

<b>Status</b> - will give you some more info about what went wrong with your request

### Filter Vehicles

This feature will return all vehicles separated by one of the following filter:
* model
* fuel type
* transmission
* dealer

<b>How to use the 'Filter Vehicles'?</b>

http://localhost:8080/vehicles/filter/{filter} where {filter} should be replaced by one of the filters

If your request doesn't have any error, the response should be like that (making the request http://localhost:8080/vehicles/filter/model):

```
{
    "result":{
        "A":[
            {
                "transmission":"AUTO",
                "fuel":"ELECTRIC",
                "dealer":"bbcdbbad-5d0b-45ef-90ac-3581b997e063",
                "model":"A",
                "id":"778a04fd-0a6a-4dc7-92bb-a7517608efc2",
                "availability":{
                    "tuesday":[
                        "1000",
                        "1030"
                    ],
                    "wednesday":[
                        "1000",
                        "1030"
                    ]
                }
            }
        ],
        "AMG":[
            {
                "transmission":"AUTO",
                "fuel":"ELECTRIC",
                "dealer":"846679bd-5831-4286-969b-056e9c89d74c",
                "model":"AMG",
                "id":"d5d0aabc-c0de-4f38-badc-759f96f5fca3",
                "availability":{
                    "tuesday":[
                        "1000",
                        "1030"
                    ],
                    "monday":[
                        "1000",
                        "1030"
                    ]
                }
            }
        ]
    },
    "code":200,
    "status":"Ok"
}
```

### Closest Dealer

This functionality will return the closest dealer to the given location that has a vehicle with the given characteristics. If no characteristics are given, it will return the closest dealer of them all. You should give the following parameters:
* coords (required) - a pair of doubles separated by a comma (ex: coords=latitude,longitude)
* model - the model that you are looking for
* fuel type - the fuel type that you are looking for
* transmission - the transmission that you are looking for

<i>Note: If model, fuel type and transmission are not given, this API will return the closest dealer of the coordinates given.</i>

<b>How to use the 'Closest Dealer'?</b>

One simple example will clarify you. Example: http://localhost:8080/dealers/closest?coords=39,10&model=AMG&fuel=GASOLINE&transmission=AUTO. And the response to that request should be something like the following example:

```
{
    "result":{
        "latitude":38.746721,
        "name":"MB Lisboa",
        "closed":[
            "sunday",
            "monday"
        ],
        "id":"bbcdbbad-5d0b-45ef-90ac-3581b997e063",
        "longitude":-9.229837
    },
    "code":200,
    "status":"Ok"
}
```

### Dealers Sorted By Distance

Given a location and the characteristics of the vehicle, this function will return all the dealers that meet the requirements, sorted by distance (the closest to the furthest of the coordinates given). In this case, you should give the following parameters:
* coords (required) - a pair of doubles separated by a comma (ex: coords=latitude,longitude)
* model - the model that you are looking for
* fuel type - the fuel type that you are looking for
* transmission - the transmission that you are looking for

<i>Note: If model, fuel type and transmission are not given, this API will return all dealers sorted by distance (the closest to the furthest of the coordinates given).</i>

<b>How to use the 'Dealers Sorted By Distance'?</b>

Example: http://localhost:8080/dealers/sorted?coords=39,10&model=AMG&fuel=ELECTRIC&transmission=AUTO. And the response to that request should be something like the following example:

```
{
    "result":[
        {
            "latitude":38.746721,
            "name":"MB Lisboa",
            "closed":[
                "sunday",
                "monday"
            ],
            "id":"bbcdbbad-5d0b-45ef-90ac-3581b997e063",
            "longitude":-9.229837
        },
        {
            "latitude":37.104404,
            "name":"MB Albufeira",
            "closed":[
                "friday",
                "wednesday"
            ],
            "id":"846679bd-5831-4286-969b-056e9c89d74c",
            "longitude":-8.236308
        },
        {
            "latitude":41.156287,
            "name":"MB Porto",
            "closed":[
                "thursday",
                "wednesday"
            ],
            "id":"d4f4d287-1ad6-4968-a8ff-e9e0009ad5d1",
            "longitude":-8.645977
        }
    ],
    "code":200,
    "status":"Ok"
}
```

### Dealers Inside of a Given Polygon

Given one polygon and the characteristics of the intended vehicle, this request will return all the dealers that have a vehicle with the specified requirements that are inside of the given polygon. In this case, you should give the following parameters:
* coordsPolygon (required) - several pairs of doubles separated by a comma (ex: coordsPolygon=latitude,longitude,latitude,longitude,latitude,longitude)
* model - the model that you are looking for
* fuel type - the fuel type that you are looking for
* transmission - the transmission that you are looking for

<i>Note: If model, fuel type and transmission are not given, API will return all dealers inside of the polygon. The polygon must have at least 3 corners.</i>

<b>How to use the 'Dealers Sorted By Distance'?</b>

Example: http://localhost:8080/dealers/polygon?coordsPolygon=40,10,42,12,36.8473,-9.046,40,-18&model=AMG&fuel=ELECTRIC&transmission=AUTO. And the response to that request should be something like the following example:

```
{
    "result":[
        {
            "latitude":37.104404,
            "name":"MB Albufeira",
            "closed":[
                "friday",
                "wednesday"
            ],
            "id":"846679bd-5831-4286-969b-056e9c89d74c",
            "longitude":-8.236308
        },
        {
            "latitude":38.746721,
            "name":"MB Lisboa",
            "closed":[
                "sunday",
                "monday"
            ],
            "id":"bbcdbbad-5d0b-45ef-90ac-3581b997e063",
            "longitude":-9.229837
        }
    ],
    "code":200,
    "status":"Ok"
}
```

### Create a New Booking

With this type of request you will be able to create a new booking (reserve a vehicle). The necessary parameters are:
* firstName (required) - the first name of the person that made the booking
* lastName (required) - the last name of the person that made the booking
* vehicleId (required) - the id of the intended vehicle
* pickupDate (required) - the date of the booking
* createdAt - when the booking was created. If not given, the API will assign the actual date to this variable

Example: http://localhost:8080/bookings/new?firstName=Goncalo&lastName=Costa&vehicleId=768a73af-4336-41c8-b1bd-76bd700378ce&pickupDate=2018-02-26T10:00:00&createdAt=2018-02-02T00:43:00

```
{
    "result":{
        "firstName":"Goncalo",
        "lastName":"Costa",
        "createdAt":"2018-02-02 00:43:00",
        "pickupDate":"2018-02-26 10:00:00",
        "isMainBooking":true,
        "id":"1",
        "vehicleId":"768a73af-4336-41c8-b1bd-76bd700378ce"
    },
    "code":201,
    "status":"Ok"
}
```

<i>Note: In this case, it is important that the code retrieved by the API be 201 (it means that the booking was created successfully). In the result, there's a 'isMainBooking' field that tells us that this booking is the first in the line to that vehicle at the pickupDate (please see the justification to that in the 'Explanations' section).</i>

### Cancel a Booking

This functionality will cancel the booking of the given id. These are the possible parameters:
* id (required) - id of the intended booking
* cancelledAt - when the booking was cancelled. If not given, the API will assign the actual date to this variable
* cancelledReason - if there is a reason, this reason should be given in this parameter

Example: http://localhost:8080/bookings/cancel?id=1&cancelledAt=2018-03-04T21:32:00&cancelledReason=Because%20I%20can

```
{
    "result":{
        "nextBooking":{},
        "cancelled":{
            "firstName":"Goncalo",
            "lastName":"Costa",
            "createdAt":"2018-02-02 00:43:00",
            "cancelledAt":"2018-03-04 21:32:00",
            "pickupDate":"2018-02-26 10:00:00",
            "isMainBooking":false,
            "id":"1",
            "vehicleId":"768a73af-4336-41c8-b1bd-76bd700378ce",
            "cancelledReason":"Because I can"
        }
    },
    "code":200,
    "status":"Ok"
}
```

<i>Note: In this case, the 'nextBooking' field will have the next booking in the line (please see the 'Explanations' section). The 'nextBooking' was given by the API to be contacted.</i>

## Explanations

There are 2 explanations that I intend to give.
- One is related to the dealers. When the user just gives the coordinates (or polygon coordinates) and doesn't give anything else, this API will retrieve the results as if the user has requested all the vehicles available.
- the other is related to the bookings. Since we have to deal with overbooking, I created the concept of the 'main booking'. It means that a vehicle could have several bookings to a date. If this scenario exists, there will be a main booking (the first made) and all others will be the secondary bookings. If the current main booking was cancelled, the next in the line will be contacted (to inform that this user has now the main booking).

<b>About the limitations and the possible improvements</b>
I think that when it is requested to the API to give the dealer that has a vehicle with the specified characteristics, the vehicles that correspond to that should also be returned in the result (I thought about it, but I didn't have the time to do it).

## Additional Info

This API was constructed based on one example of the Spring tutorial (https://spring.io/guides/gs/rest-service/). The code in src/main/java/app/Geometry/Polygon.java (function contains) can be found in this site: https://wrf.ecse.rpi.edu//Research/Short_Notes/pnpoly.html.
My unit tests only evaluate the main functions of each class.
