# Postal Code Validation Info

## Rest service for querying country-specific postal code validations

This project is a simple REST service built using Spring Boot, Maven, and an in-memory H2 database.
The service provides information for validating postal codes for any country and the ability to add countries to query
validations on.

### Table of Contents

- [Features](#features)
- [Technologies Used](#technologies-used)
- [Getting Started](#getting-started)
    - [Prerequisites](#prerequisites)
    - [Installation](#installation)
- [Usage](#usage)
    - [Endpoints](#endpoints)
        - [Get Postal Code Validation Information](#get-postal-code-validation-information)
        - [Add New Country](#add-new-country)
    - [Example Requests](#example-requests)
    - [Example Errors](#example-errors)
    - [Adding new field](#adding-new-field)
- [Contributing](#contributing)
- [License](#license)

### Features

- Get postal code validation details by country code. Note that you need to add the country first (see below).
- Add new countries for retrieving postal code validations by posting the country code to the add-country endpoint.
  TODO: add link

### Technologies Used

Postal Code Validation Info may also be compatible with other versions, but is tested with the following stack:

- Java 17
- Spring Boot 3.3.1
- Maven 3.9.8
- H2 Database 2.2.224

### Getting Started

#### Prerequisites

- Java 17 or later
- Maven 3.6.0 or later

#### Installation

1. Clone the repository:
    ```sh
    git clone https://github.com/Jasperav/PostalCodeValidationInfoService.git
    cd PostalCodeValidationInfoService
    ```

2. Build the project:
    ```sh
    mvn clean install
    ```

3. Run the application:
    ```sh
    mvn spring-boot:run
    ```

### Usage

See the examples.http for examples of all requests, or use the [CURL commands](#example-requests)

#### Endpoints

##### Get Postal Code Validation Information

- URL: `/api/v1/postal-code-validations/{countryCode}`
- Method: GET
- Query Parameter: None
- Description: Retrieves the postal code validation information for the specified country code.
- Response example for path parameter `countryCode` 'NL':

```json
{
  "countryName": "Netherlands",
  "postalCode": {
    "format": "#### @@",
    "regex": "^(\\d{4}[A-Z]{2})$"
  }
}
```

##### Add New Country

- URL: `/api/v1/countries/{countryCode}`
- Method: POST
- Query Parameter: None
- Description: Adds a new country code which can be used later to retrieve postal code validation information. The country code must be one of the following formats: cca2, ccn3, cca3 or cioc.
- Note that HTTP status code 201 (CREATED) is used if the country did not exist in the database yet, and 200 is used if the country was already in the database.
- Response example for path parameter `countryCode` 'NL':

```json
{
  "countryName": "Netherlands",
  "postalCode": {
    "format": "#### @@",
    "regex": "^(\\d{4}[A-Z]{2})$"
  }
}
```

#### Example Requests

Get Postal Code Information:

```sh
curl -X GET "http://localhost:8080/api/v1/postal-code-validations/NL"
```

Add New Country:

```sh
curl -X POST "http://localhost:8080/api/v1/countries/NL"
```

### Example Errors

Below is the full list of possible errors:

#### Country Code Not Found

- Status Code: 404 Not Found
- Description: The specified country code does not exist in the database. Maybe the user needs to add it first.
- Response: empty body

### Invalid Country Code Format

- Status Code: 400 Bad Request
- Description: The provided country code format is invalid.
- Response: empty body

#### Internal server failure

- Status Code: 500 Internal server error
- Description: Failed to fetch details from the external API.
- Response: empty body

#### Adding new field
1. Add another property in model **Country**
2. Add the field in **CountryService**#_EXTERNAL_API_FIELDS_
3. Update the JSON property in **TestDeserializeCountryApiJson**
4. Run the tests to see if everything passes

### Contributing

You can fork the project and create a PR, but this is just a small project which probably shouldn't be used in
production :).

### License

MIT licence; _Do whatever you want with my stuff, just don't sue me_
