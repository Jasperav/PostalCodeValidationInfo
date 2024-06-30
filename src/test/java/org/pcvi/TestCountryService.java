package org.pcvi;

import jakarta.annotation.Nonnull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.pcvi.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Integration test to test service CountryService.
 * It tests the public methods, including testing if a country can be added to the database and an error if the country
 * is invalid.
 * The webEnvironment is not the default MOCK since it's useful in this scenario to actually test the whole flow (and short on time).
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// See https://stackoverflow.com/q/14718088
// This is needed to reset the H2 database for every test, it's needed because the HTTP status codes are checked, and no cache should exist
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TestCountryService {

    private static final String VALID_COUNTRY_CODE = "NL";
    private static final String INVALID_COUNTRY_CODE = "JaspersImperium";

    @Autowired
    private CountryService countryService;

    /**
     * Adds a country to the database if it's valid, and provides a mandatory check on the result
     */
    private void addCountry(@Nonnull HttpStatusCode httpStatusCode, @Nonnull String countryCode) {
        assertEquals(httpStatusCode, countryService.addCountry(countryCode).getStatusCode());
    }

    /**
     * Tests the `addCountry` method.
     * It checks for a success case and also tests that when the user inputs an invalid country, an error is returned.
     * It checks that the same country can be added again without an issue
     */
    @Nested
    class AddCountry {

        @Test
        @DisplayName("Tests that a valid country code can be added, and that another call to the service won't give an error (adding a country twice for whatever reason).")
        void testSuccess() {
            addCountry(HttpStatus.CREATED, VALID_COUNTRY_CODE);
            addCountry(HttpStatus.OK, VALID_COUNTRY_CODE);
        }

        @Test
        @DisplayName("Tests that a invalid country code can not be added.")
        void testFailure() {
            addCountry(HttpStatus.BAD_REQUEST, INVALID_COUNTRY_CODE);
        }
    }

    /**
     * Tests the `queryCountry` method.
     * It doesn't make sense to test an invalid country code here, since it will never be added to the database in the first place.
     */
    @Nested
    class QueryCountry {

        @Test
        @DisplayName("Tests that a valid country code is not in the database from the beginning, but after adding it, it can be found")
        void testSuccess() {
            queryCountry(false);
            addCountry(HttpStatus.CREATED, VALID_COUNTRY_CODE);
            queryCountry(true);
        }

        private void queryCountry(boolean shouldSucceed) {
            assertEquals(shouldSucceed, countryService.queryCountry(TestCountryService.VALID_COUNTRY_CODE).isPresent());
        }
    }
}
