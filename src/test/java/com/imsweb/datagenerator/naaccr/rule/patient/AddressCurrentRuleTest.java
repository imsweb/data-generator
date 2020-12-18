package com.imsweb.datagenerator.naaccr.rule.patient;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.naaccrxml.entity.Patient;

public class AddressCurrentRuleTest {

    private final AddressCurrentRule _rule = new AddressCurrentRule();

    @Test
    public void testExecute() {
        Patient patient = new Patient();

        // if no state is available, address fields should be blank
        Map<String, Object> context = new HashMap<>();
        _rule.execute(patient, null, context);
        Assert.assertNull(patient.getItemValue("addrCurrentCity"));
        Assert.assertNull(patient.getItemValue("addrCurrentState"));
        Assert.assertNull(patient.getItemValue("addrCurrentPostalCode"));
        Assert.assertNull(patient.getItemValue("addrCurrentNoStreet"));
        Assert.assertNull(patient.getItemValue("countyCurrent"));

        // generate 10 random addresses and verify pattern for each
        NaaccrDataGeneratorOptions options = new NaaccrDataGeneratorOptions();
        options.setState("MD");
        for (int i = 0; i < 10; i++) {
            _rule.execute(patient, options, context);

            // verify city name pattern
            Assert.assertTrue(patient.getItemValue("addrCurrentCity").matches("^[\\w .]+$"));
            // verify 2 letter state
            Assert.assertTrue(patient.getItemValue("addrCurrentState").matches("^[A-Z]{2}$"));
            // verify 5 or 9 digit zip code
            Assert.assertTrue(patient.getItemValue("addrCurrentPostalCode").matches("^\\d{5}$") || patient.getItemValue("addressCurrentPostalCode").matches("^\\d{9}$"));
            // verify length and pattern of street name
            Assert.assertTrue(patient.getItemValue("addrCurrentNoStreet").length() <= 60);
            Assert.assertTrue(patient.getItemValue("addrCurrentNoStreet").matches("^\\d+ [\\w .]+ \\w+\\.?$"));
            // verify 3 digit county code
            Assert.assertTrue(patient.getItemValue("countyCurrent").matches("^\\d{3}$"));
        }
    }
}
