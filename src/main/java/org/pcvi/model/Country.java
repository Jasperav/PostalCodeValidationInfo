package org.pcvi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nonnull;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.HashMap;

/**
 * This provides the absolute basic country information there is for the output needed.
 * If you want to add more another field, see Readme.md#Adding-new-field.
 *
 * @JsonProperty is preferred over a DTO, since it are a few fields, easily maintainable and prevents a mapper and DTO class
 * which would make things overcomplicated.
 *
 * Using a Record is not possible/straightforward (I think) because some kind of object mapper is needed to map the nested properties and every field is final.
 * For some (sad) reason, @JsonProperty does not support dots to say you want to read out a nested property.
 */
@Entity
@Data
public class Country {
    /**
     * There shouldn't be another country with the same country code, that's why this is used as a primary key.
     * This is the country code in cca2, ccn3, cca3 or cioc format, e.g. 'NL'.
     */
    @Id
    private String countryCode;

    /**
     * The English country name based on the country code, e.g. 'Netherlands'.
     */
    private String countryName;

    /**
     * Since there is (currently) no check if two countries share the same postal code format and regex, no additional
     * table 'PostalCode' is added.
     * If that table would be added with the current logic, every country refers to a unique row in PostalCode and things
     * would be unnecessary complex and slower. It benefits more than having a separate namespace/object for the postal code.
     *
     * The format of a postal code, e.g. for The Netherlands it's '#### @@'
     */
    private String postalCodeFormat;

    /**
     * The regex in which postal codes can be validated, e.g. for The Netherlands it's '^(\d{4}[A-Z]{2})$'
     */
    private String postalCodeRegex;

    // region JSON unpackers which will map the nested objects in a flattened way
    @JsonProperty("name")
    private void unpackName(@Nonnull HashMap<String, Object> somePath) {
        countryName = (String) somePath.get("common");
    }

    @JsonProperty("postalCode")
    private void unpackPostalCode(@Nonnull HashMap<String, Object> somePath) {
        postalCodeFormat = (String) somePath.get("format");
        postalCodeRegex = (String) somePath.get("regex");
    }
    // endregion
}
