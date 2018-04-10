/*
 * Copyright (C) 2018 Information Management Services, Inc.
 */
package com.imsweb.datagenerator.provider.facility.rule;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.imsweb.datagenerator.provider.ProviderDataGeneratorOptions;

public class FacilityRuleTest {

    private FacilityRule _rule = new FacilityRule();

    @Test
    public void testExecute() {

        for (String state : Arrays.asList("CA", "AK", "NV")) {

            Map<String, String> provider = new HashMap<>();
            ProviderDataGeneratorOptions options = new ProviderDataGeneratorOptions();
            options.setState(state);
            _rule.execute(provider, options);

            Assert.assertTrue(provider.get("npi").matches("\\d{10}"));
            Assert.assertTrue(provider.get("name").length() > 0);
            Assert.assertTrue(provider.get("addressState").equals(state));
        }
    }
}
