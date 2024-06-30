package org.pcvi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.pcvi.model.Country;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Unit test class to test deserialization from the external API to the model class Country.
 * It includes the JSON which is sent back from the external API as a string literal, so no external call is needed
 * to run this test.
 */
@Slf4j
public class TestDeserializeCountryApiJson {

    @Test
    @DisplayName("Tests if the JSON from the external API can be converted to model class Country")
    void testJson() throws JsonProcessingException {
        var json = """
                {
                  "postalCode": {
                    "format": "#### @@",
                    "regex": "^(\\\\d{4}[A-Z]{2})$"
                  },
                  "name": {
                    "common": "Netherlands",
                    "official": "Kingdom of the Netherlands",
                    "nativeName": {
                      "nld": {
                        "official": "Koninkrijk der Nederlanden",
                        "common": "Nederland"
                      }
                    }
                  }
                }
                """;
        var objectMapper = new ObjectMapper();
        var country = objectMapper.readValue(json, Country.class);

        log.info("Country: {}", country);

        // This should not be mapped...
        assertNull(country.getCountryCode());

        // ... But these fields should be mapped
        assertEquals("Netherlands", country.getCountryName());
        assertEquals("#### @@", country.getPostalCodeFormat());
        assertEquals("^(\\d{4}[A-Z]{2})$", country.getPostalCodeRegex());
    }
}
