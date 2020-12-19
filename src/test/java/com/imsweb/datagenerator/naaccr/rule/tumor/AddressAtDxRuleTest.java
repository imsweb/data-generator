package com.imsweb.datagenerator.naaccr.rule.tumor;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.imsweb.datagenerator.naaccr.NaaccrDataGeneratorOptions;
import com.imsweb.naaccrxml.entity.Patient;
import com.imsweb.naaccrxml.entity.Tumor;

public class AddressAtDxRuleTest {

    private final AddressAtDxRule _rule = new AddressAtDxRule();

    @Test
    public void testExecute() {
        Patient patient = new Patient();

        Tumor tumor = new Tumor();

        // if not state is available, address fields should be blank
        Map<String, Object> context = new HashMap<>();
        _rule.execute(tumor, patient, null, context);
        Assert.assertNull(tumor.getItemValue("addrAtDxCity"));
        Assert.assertNull(tumor.getItemValue("addrAtDxState"));
        Assert.assertNull(tumor.getItemValue("addrAtDxPostalCode"));
        Assert.assertNull(tumor.getItemValue("addrAtDxNoStreet"));
        Assert.assertNull(tumor.getItemValue("countyAtDx"));

        // generate 10 random addresses and verify pattern for each
        NaaccrDataGeneratorOptions options = new NaaccrDataGeneratorOptions();
        options.setState("MD");
        for (int i = 0; i < 10; i++) {
            _rule.execute(tumor, patient, options, context);

            // verify city name pattern
            Assert.assertTrue("Iteration #" + i, tumor.getItemValue("addrAtDxCity").matches("^[\\w .]+$"));
            // verify 2 letter state
            Assert.assertTrue("Iteration #" + i, tumor.getItemValue("addrAtDxState").matches("^[A-Z]{2}$"));
            // verify 5 or 9 digit zip code
            Assert.assertTrue("Iteration #" + i, tumor.getItemValue("addrAtDxPostalCode").matches("^\\d{5}$") || tumor.getItemValue("addressCurrentPostalCode").matches("^\\d{9}$"));
            // verify length and pattern of street name
            Assert.assertTrue("Iteration #" + i, tumor.getItemValue("addrAtDxNoStreet").length() <= 60);
            Assert.assertTrue("Iteration #" + i, tumor.getItemValue("addrAtDxNoStreet").matches("^\\d+ [\\w .]+ \\w+\\.?$"));
            // verify 3 digit county code
            Assert.assertTrue("Iteration #" + i, tumor.getItemValue("countyAtDx").matches("^\\d{3}$"));
        }
    }
}
