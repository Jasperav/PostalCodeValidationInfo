package org.pcvi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.pcvi.model.Country;
import org.pcvi.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * A Rest controller which exposes an endpoint to add a country based on a country code.
 */
@RestController
@RequestMapping("/api/v1/countries")
@Slf4j
public class CountryController {

    @Autowired
    private CountryService countryService;

    @PostMapping("/{countryCode}")
    @Operation(summary = "Adds a new country to retrieve postal code validations for.", responses = {
            @ApiResponse(responseCode = "200", description = "Ok, the country was already in the database", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Country.class)
            )),
            @ApiResponse(responseCode = "201", description = "Created, the country wasn't in the database before", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Country.class)
            )),
            @ApiResponse(responseCode = "400", description = "Bad request, the country code was is in an invalid format. Please use one of the following formats: cca2, ccn3, cca3 or cioc"),
            @ApiResponse(responseCode = "500", description = "Internal server error occurred, please try again later")
    }, parameters = {
            @Parameter(
                    name = "countryCode",
                    description = "The country code to add to the database. Please use one of the following formats: cca2, ccn3, cca3 or cioc",
                    required = true,
                    example = "NL"
            )
    })
    public ResponseEntity<Country> addCountry(@PathVariable("countryCode") @NotBlank String countryCode) {
        log.debug("Received a request to add country with country code: '{}'", countryCode);

        return countryService.addCountry(countryCode);
    }
}