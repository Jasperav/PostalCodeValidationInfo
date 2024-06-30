package org.pcvi.service;

import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.pcvi.model.Country;
import org.pcvi.repository.CountryRepository;
import org.slf4j.helpers.CheckReturnValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

/**
 * The service to add a Country and retrieve one.
 * Tested in com.pcvi.TestCountryService
 */
@Service
@Slf4j
public class CountryService {

    /**
     * The base URL for making the call to the external API which provides information about the postal code
     * Note that you need to construct this further with a country code and optional fields.
     */
    private final String EXTERNAL_API_URL = "https://restcountries.com/v3.1/alpha/";

    /**
     * For a full list, see https://gitlab.com/restcountries/restcountries/-/blob/master/FIELDS.md.
     * Note that if you add another field here, update the example requests and the Country object as well.
     */
    private final String EXTERNAL_API_FIELDS = "postalCode,name";

    @Autowired
    private CountryRepository countryRepository;

    /**
     * Queries a country based on the countryCode parameter in the local database
     */
    @CheckReturnValue
    public Optional<Country> queryCountry(@Nonnull String countryCode) {
        log.debug("Querying database for country code: '{}'", countryCode);

        return countryRepository.findById(countryCode);
    }

    /**
     * Adds a country to the database, or returns the country from the database if it already exists.
     * If the country can not be retrieved from the external API, a 502 Bad Request response will be returned.
     */
    @CheckReturnValue
    public ResponseEntity<Country> addCountry(@Nonnull String countryCode) {
        var existingCountry = queryCountry(countryCode);

        if (existingCountry.isPresent()) {
            log.debug("Country already exists in the database: '{}'", existingCountry.get());

            return ResponseEntity.ok(existingCountry.get());
        }

        log.debug("Country did not exists in the database, make an API call to try to retrieve it");

        return queryCountryFromApi(countryCode);
    }

    /**
     * Makes a call to the external API to retrieve a Country if a valid countryCode is passed in.
     * If the country can not be retrieved from the external API, a 502 Bad Request response will be returned.
     */
    @CheckReturnValue
    private ResponseEntity<Country> queryCountryFromApi(String countryCode) {
        var restTemplate = new RestTemplate();

        try {
            var requestEntityCountry = restTemplate.getForEntity(EXTERNAL_API_URL + countryCode + "?fields=" + EXTERNAL_API_FIELDS, Country.class);

            log.debug("Retrieved country from external API: '{}'", requestEntityCountry);

            var country = requestEntityCountry.getBody();

            // Don't check if the postal codes are filled, because many countries do not have postal codes, see also:
            // https://opencagedata.com/guides/how-to-think-about-postcodes-and-geocoding#:~:text=No%2C%20many%20countries%20do%20not,it%20is%20not%20widely%20used.
            // But every country should have a country name, so that should be filled.
            // The country code should remain empty, this is not requested from the external API (and it's already in memory anyway).
            if (country == null || !StringUtils.hasLength(country.getCountryName()) || country.getCountryCode() != null) {
                // Return for now a bad request, this should never happen and a fix needs to be made if for some reason this block
                // of code is executed. It's not pretty but again: this case should not happen in the first place.
                return ResponseEntity.badRequest().build();
            }

            country.setCountryCode(countryCode);

            countryRepository.save(country);

            return requestEntityCountry;
        } catch (HttpClientErrorException.BadRequest request) {
            log.warn("Got bad request while trying to retrieve the country code: '{}'. This could indicate an invalid country code, but it's not clear from the bad request, so it can not be anticipated on.", countryCode, request);

            return ResponseEntity.badRequest().build();
        }
    }
}