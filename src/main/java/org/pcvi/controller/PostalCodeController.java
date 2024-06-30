package org.pcvi.controller;

import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.pcvi.model.Country;
import org.pcvi.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * A Rest controller which exposes an endpoint to query a country based on a country code.
 */
@RestController
@RequestMapping("/api/v1/postal-code-validations")
@Slf4j
public class PostalCodeController {

    @Autowired
    private CountryService countryService;

    /**
     * Returns a Country based on the countryCode if it was added before in CountryController.
     * Will return a BAD_REQUEST response if the country wasn't added before.
     */
    @GetMapping("/{countryCode}")
    public ResponseEntity<Country> queryCountry(@PathVariable("countryCode") @NotBlank String countryCode) {
        log.debug("Received a request to query country with country code '{}'", countryCode);

        var country = countryService.queryCountry(countryCode);

        // See also https://stackoverflow.com/a/58531279/7715250, this will automatically throw a 404 if the argument is null
        return ResponseEntity.of(country);
    }
}