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
        Map<String, Object> context = new HashMap<>();
        _rule.execute(rec, null, null, context);
        Assert.assertNull(rec.get("addrCurrentCity"));
        Assert.assertNull(rec.get("addrCurrentState"));
        Assert.assertNull(rec.get("addrCurrentPostalCode"));
        Assert.assertNull(rec.get("addrCurrentNoStreet"));
        Assert.assertNull(rec.get("countyCurrent"));

        // generate 10 random addresses and verify pattern for each
        NaaccrDataGeneratorOptions options = new NaaccrDataGeneratorOptions();
        options.setState("MD");
        for (int i = 0; i < 10; i++) {
            rec.clear();
            _rule.execute(rec, null, options, context);

            // verify city name pattern
            Assert.assertTrue(rec.get("addrCurrentCity").matches("^[\\w .]+$"));
            // verify 2 letter state
            Assert.assertTrue(rec.get("addrCurrentState").matches("^[A-Z]{2}$"));
            // verify 5 or 9 digit zip code
            Assert.assertTrue(rec.get("addrCurrentPostalCode").matches("^\\d{5}$") || rec.get("addressCurrentPostalCode").matches("^\\d{9}$"));
            // verify length and pattern of street name
            Assert.assertTrue(rec.get("addrCurrentNoStreet").length() <= 60);
            Assert.assertTrue(rec.get("addrCurrentNoStreet").matches("^\\d+ [\\w .]+ \\w+\\.?$"));
            // verify 3 digit county code
            Assert.assertTrue(rec.get("countyCurrent").matches("^\\d{3}$"));
        }
    }
}
