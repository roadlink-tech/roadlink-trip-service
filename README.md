# trip-service

## Dev Tools
### Add section
This scripts creates a section with the given data and completing the information about the 
trip point using the geoapify api and saving it in the repository.

This script can be invoked through http with a POST request to `/trip-service/dev-tools/add-section`
with body
```
{
    "departure": {
        "name": "AvCabildo 4853",
        "at": "2022-10-15T12:00:00Z"
    },
    "arrival": {
        "name": "AvCabildo 20",
        "at": "2022-10-15T13:00:00Z"
    },
    "distanceInMeters": 6070.0,
    "driver": "John Smith",
    "vehicle": "Ford mustang",
    "availableSeats": 4
}
```