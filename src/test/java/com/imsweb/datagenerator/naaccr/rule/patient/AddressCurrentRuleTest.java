package com.imsweb.datagenerator.naaccr.rule.patient;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;

public class AddressCurrentRuleTest {

    private AddressCurrentRule _rule = new AddressCurrentRule();

    @Test
    public void testExecute() {

        // if not state is available, address fields should be blank
        Map<String, String> rec = new HashMap<>();
        _rule.execute(rec, null, null);
        Assert.assertNull(rec.get("addressCurrentCity"));
        Assert.assertNull(rec.get("addressCurrentState"));
        Assert.assertNull(rec.get("addressCurrentPostalCode"));
        Assert.assertNull(rec.get("addressCurrentStreetName"));
        Assert.assertNull(rec.get("addressCurrentCounty"));

        // generate 10 random addresses and verify pattern for each
        NaaccrDataGeneratorOptions options = new NaaccrDataGeneratorOptions();
        options.setState("MD");
        for (int i = 0; i < 10; i++) {
            rec.clear();
            _rule.execute(rec, null, options);

            // verify city name pattern
            Assert.assertTrue(rec.get("addressCurrentCity").matches("^[\\w \\.]+$"));
            // verify 2 letter state
            Assert.assertTrue(rec.get("addressCurrentState").matches("^[A-Z]{2}$"));
            // verify 5 or 9 digit zip code
            Assert.assertTrue(rec.get("addressCurrentPostalCode").matches("^\\d{5}$") || rec.get("addressCurrentPostalCode").matches("^\\d{9}$"));
            // verify length and pattern of street name
            Assert.assertTrue(rec.get("addressCurrentStreetName").length() <= 60);
            Assert.assertTrue(rec.get("addressCurrentStreetName").matches("^\\d+ [\\w \\.]+ \\w+\\.?$"));
            // verify 3 digit county code
            Assert.assertTrue(rec.get("addressCurrentCounty").matches("^\\d{3}$"));
        }
    }
}
