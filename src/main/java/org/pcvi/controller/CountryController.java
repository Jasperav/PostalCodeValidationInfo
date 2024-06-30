package org.pcvi.controller;

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

    /**
     * Returns a Country based on the countryCode if it was successfully added.
     * Will return a BAD_REQUEST response if the country code is invalid, or the external api can not be reached.
     */
    @PostMapping("/{countryCode}")
    public ResponseEntity<Country> addCountry(@PathVariable("countryCode") @NotBlank String countryCode) {
        log.debug("Received a request to add country with country code: '{}'", countryCode);

        return countryService.addCountry(countryCode);
    }
}